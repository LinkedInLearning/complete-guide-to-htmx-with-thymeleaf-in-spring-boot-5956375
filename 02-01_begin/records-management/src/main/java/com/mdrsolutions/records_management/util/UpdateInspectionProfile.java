package com.mdrsolutions.records_management.util;

import org.w3c.dom.*;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.nio.file.*;
import java.util.HashSet;
import java.util.Set;

public class UpdateInspectionProfile {

    // List of custom Thymeleaf attributes
    private static final String[] CUSTOM_ATTRIBUTES = {
            "th:text", "th:each", "th:if", "th:unless", "th:href", "layout:fragment", "th:src",
            "th:action", "th:class", "th:attr", "th:abbr", "th:accept", "th:accept-charset",
            "th:accesskey", "th:align", "th:alt", "th:archive", "th:audio", "th:autocomplete",
            "th:axis", "th:background", "th:bgcolor", "th:border", "th:cellpadding", "th:cellspacing",
            "th:challenge", "th:charset", "th:cite", "th:classid", "th:codebase", "th:codetype",
            "th:cols", "th:colspan", "th:compact", "th:content", "th:contenteditable", "th:contextmenu",
            "th:data", "th:datetime", "th:dir", "th:draggable", "th:dropzone", "th:enctype",
            "th:for", "th:form", "th:formaction", "th:formenctype", "th:formmethod", "th:formtarget",
            "th:frame", "th:frameborder", "th:headers", "th:height", "th:high", "th:hreflang",
            "th:hspace", "th:http-equiv", "th:icon", "th:id", "th:keytype", "th:kind", "th:label",
            "th:lang", "th:list", "th:longdesc", "th:low", "th:manifest", "th:marginheight",
            "th:marginwidth", "th:max", "th:maxlength", "th:media", "th:method", "th:min",
            "th:name", "th:optimum", "th:pattern", "th:placeholder", "th:poster", "th:preload",
            "th:radiogroup", "th:rel", "th:rev", "th:rows", "th:rowspan", "th:rules", "th:sandbox",
            "th:scheme", "th:scope", "th:scrolling", "th:size", "th:sizes", "th:span", "th:spellcheck",
            "th:srclang", "th:standby", "th:start", "th:step", "th:style", "th:summary",
            "th:tabindex", "th:target", "th:title", "th:type", "th:usemap", "th:value",
            "th:valuetype", "th:vspace", "th:width", "th:wrap", "th:xmlbase", "th:xmllang",
            "th:xmlspace", "th:with", "th:fragment", "th:case", "th:switch", "th:object", "th:utext",
            "layout:decorate" , "th:insert"
    };

    public static void main(String[] args) {
        try {
            Path projectRoot = Paths.get("").toAbsolutePath();
            Path inspectionProfilePath = projectRoot.resolve(".idea/inspectionProfiles/Project_Default.xml");

            // Ensure that the inspectionProfiles directory exists
            File directory = inspectionProfilePath.getParent().toFile();
            if (!directory.exists()) {
                System.out.println("Directory does not exist. Creating: " + directory);
                if (directory.mkdirs()) {
                    System.out.println("Directory created successfully.");
                } else {
                    System.err.println("Failed to create directory.");
                    return;
                }
            }

            // If the file doesn't exist, create it
            if (Files.notExists(inspectionProfilePath)) {
                createDefaultXml(inspectionProfilePath.toFile());
            }

            // Load the existing XML file
            File xmlFile = inspectionProfilePath.toFile();
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(xmlFile);
            doc.getDocumentElement().normalize();

            // Update the XML with the custom attributes
            updateXmlWithAttributes(doc);

            // Save the updated XML back to the file
            saveXml(doc, xmlFile);

            System.out.println("Project_Default.xml updated successfully!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Function to create a new Project_Default.xml file with the basic structure
    private static void createDefaultXml(File file) throws Exception {
        System.out.println("Creating a new Project_Default.xml...");
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.newDocument();

        // Create the root element
        Element component = doc.createElement("component");
        component.setAttribute("name", "InspectionProjectProfileManager");
        doc.appendChild(component);

        // Create the profile
        Element profile = doc.createElement("profile");
        profile.setAttribute("version", "1.0");
        component.appendChild(profile);

        // Add options and inspection tool
        Element optionName = doc.createElement("option");
        optionName.setAttribute("name", "myName");
        optionName.setAttribute("value", "Project Default");
        profile.appendChild(optionName);

        Element inspectionTool = doc.createElement("inspection_tool");
        inspectionTool.setAttribute("class", "HtmlUnknownAttribute");
        inspectionTool.setAttribute("enabled", "true");
        inspectionTool.setAttribute("level", "WARNING");
        inspectionTool.setAttribute("enabled_by_default", "true");
        profile.appendChild(inspectionTool);

        Element optionValues = doc.createElement("option");
        optionValues.setAttribute("name", "myValues");
        inspectionTool.appendChild(optionValues);

        Element value = doc.createElement("value");
        optionValues.appendChild(value);

        Element list = doc.createElement("list");
        list.setAttribute("size", "0");
        value.appendChild(list);

        Element customValues = doc.createElement("option");
        customValues.setAttribute("name", "myCustomValuesEnabled");
        customValues.setAttribute("value", "true");
        inspectionTool.appendChild(customValues);

        // Save the new XML file
        saveXml(doc, file);
    }

    // Function to update the XML with custom attributes
    private static void updateXmlWithAttributes(Document doc) {
        NodeList listNodes = doc.getElementsByTagName("list");
        if (listNodes.getLength() > 0) {
            Element listElement = (Element) listNodes.item(0);
            NodeList items = listElement.getElementsByTagName("item");

            // Collect existing attributes
            Set<String> existingAttributes = new HashSet<>();
            for (int i = 0; i < items.getLength(); i++) {
                Element item = (Element) items.item(i);
                existingAttributes.add(item.getAttribute("itemvalue"));
            }

            // Add missing attributes
            int index = items.getLength();
            for (String attr : CUSTOM_ATTRIBUTES) {
                if (!existingAttributes.contains(attr)) {
                    Element newItem = doc.createElement("item");
                    newItem.setAttribute("index", String.valueOf(index));
                    newItem.setAttribute("class", "java.lang.String");
                    newItem.setAttribute("itemvalue", attr);
                    listElement.appendChild(newItem);
                    index++;
                }
            }

            // Update the size attribute
            listElement.setAttribute("size", String.valueOf(index));
        }
    }

    // Function to save the updated XML
    private static void saveXml(Document doc, File file) throws TransformerException {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(file);
        transformer.transform(source, result);
    }
}
