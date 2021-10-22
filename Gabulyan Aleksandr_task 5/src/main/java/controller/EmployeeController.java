package main.java.controller;

import main.java.model.Employee;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/employee")
public class EmployeeController {
    File file = new File("src/main/resources/file.xml");

    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    DocumentBuilder builder = factory.newDocumentBuilder();
    Document document = builder.parse(file);

    public EmployeeController() throws ParserConfigurationException, IOException, SAXException {
    }

    @GetMapping
    public ResponseEntity<List<Employee>> getEmployeeBySurname(@RequestParam("surname") String surname) {
        List<Employee> list = new ArrayList<>();
        try {
            XPathFactory xPathFactory = XPathFactory.newInstance();
            XPath xPath = xPathFactory.newXPath();
            NodeList nodeList = (NodeList) xPath.compile("/Office/Employee[surname = '" +
                            surname.substring(0, 1).toUpperCase() + surname.substring(1) + "']/id/text()")
                    .evaluate(document, XPathConstants.NODESET);

            for (int i = 0; i < nodeList.getLength(); i++) {
                NodeList nodeList1 = document.getDocumentElement().getElementsByTagName("Employee");
                NodeList childNodes = nodeList1.item(Integer.parseInt(nodeList.item(i).getTextContent()) - 1).getChildNodes();

                Employee employee = new Employee();

                int j = 1;
                employee.setId(Integer.parseInt(childNodes.item(j).getTextContent()));
                j += 2;
                employee.setName(childNodes.item(j).getTextContent());
                j += 2;
                employee.setSurname(childNodes.item(j).getTextContent());
                j += 2;
                employee.setPosition(childNodes.item(j).getTextContent());
                j += 2;
                employee.setSalary(Integer.parseInt(childNodes.item(j).getTextContent()));

                list.add(employee);
            }
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getAllEmployee() {
        List<Employee> list = new ArrayList<>();
        try {
            factory = DocumentBuilderFactory.newInstance();
            builder = factory.newDocumentBuilder();
            document = builder.parse(file);

            NodeList nodeList = document.getDocumentElement().getElementsByTagName("Employee");
            for (int i = 0; i < nodeList.getLength(); i++) {
                NodeList childNodes = nodeList.item(i).getChildNodes();
                Employee employee = new Employee();

                int j = 1;
                employee.setId(Integer.parseInt(childNodes.item(j).getTextContent()));
                j += 2;
                employee.setName(childNodes.item(j).getTextContent());
                j += 2;
                employee.setSurname(childNodes.item(j).getTextContent());

                list.add(employee);
            }
        } catch (ParserConfigurationException | IOException | SAXException e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(list.toString(), HttpStatus.OK);
    }

    @PostMapping("/salary")
    public void changeSalary(@RequestBody Employee employee) {
        try {
            document.getDocumentElement().normalize();
            NodeList nodeList1 = document.getDocumentElement().getElementsByTagName("Employee");
            nodeList1.item(employee.getId() - 1).getChildNodes().item(9).getFirstChild().setNodeValue(String.valueOf(employee.getSalary()));

            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.METHOD, "xml");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

            DOMSource domSource = new DOMSource(document);
            StreamResult result = new StreamResult(file);
            transformer.transform(domSource, result);
        } catch (TransformerException e) {
            e.printStackTrace();
        }
    }

    @PostMapping("/new")
    public ResponseEntity<Integer> addEmployee(@RequestBody Employee employee) {
        int addedId = -1;
        try {
            Element documentElement = document.getDocumentElement();

            NodeList nodeList = documentElement.getElementsByTagName("Employee");
            int next_id = nodeList.getLength() + 1;


            Element e_employee = document.createElement("Employee");
            documentElement.appendChild(e_employee);

            Element e_id = document.createElement("id");
            e_id.setTextContent(String.valueOf(next_id));
            e_employee.appendChild(e_id);

            Element e_name = document.createElement("name");
            e_name.setTextContent(employee.getName());
            e_employee.appendChild(e_name);

            Element e_surname = document.createElement("surname");
            e_surname.setTextContent(employee.getSurname());
            e_employee.appendChild(e_surname);

            Element e_position = document.createElement("position");
            e_position.setTextContent(employee.getPosition());
            e_employee.appendChild(e_position);

            Element e_salary = document.createElement("salary");
            e_salary.setTextContent(Integer.toString(employee.getSalary()));
            e_employee.appendChild(e_salary);

            document.replaceChild(documentElement, documentElement);

            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.METHOD, "xml");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

            Source source = new DOMSource(document);
            Result result = new StreamResult(new File("src/main/resources/file.xml"));
            transformer.transform(source, result);

            addedId = Integer.parseInt(documentElement.getLastChild().getChildNodes().item(0).getTextContent());
        } catch (TransformerException e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(addedId, HttpStatus.OK);
    }
}