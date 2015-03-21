//package com.mom.app.model.xml;
//
//import android.util.Log;
//
//import com.mom.app.model.pbxpl.ImpsVerifyProcessResult;
//
//import org.xmlpull.v1.XmlPullParser;
//import org.xmlpull.v1.XmlPullParserException;
//import org.xmlpull.v1.XmlPullParserFactory;
//
//import java.io.IOException;
//import java.io.InputStream;
//
///**
// * Created by Pbx12 on 2/5/2015.
// */
//public class XMLPullParserHandler {
//    ImpsVerifyProcessResult employees;
//    private Employee employee;
//    private String text;
//
//    public XMLPullParserHandler() {
//        employees = new ArrayList<Employee>();
//    }
//
//    public ImpsVerifyProcessResult getEmployees() {
//        return employees;
//    }
//
//    public ImpsVerifyProcessResult parse(InputStream is) {
//        XmlPullParserFactory factory = null;
//        XmlPullParser parser = null;
//        try {
//            factory = XmlPullParserFactory.newInstance();
//            factory.setNamespaceAware(true);
//            parser = factory.newPullParser();
//
//            parser.setInput(is, null);
//
//            int eventType = parser.getEventType();
//            while (eventType != XmlPullParser.END_DOCUMENT) {
//                String tagname = parser.getName();
//                switch (eventType) {
//                    case XmlPullParser.START_TAG:
//                        if (tagname.equalsIgnoreCase("employee")) {
//                            // create a new instance of employee
//                            employee = new Employee();
//                        }
//                        break;
//
//                    case XmlPullParser.TEXT:
//                        text = parser.getText();
//                        break;
//
//                    case XmlPullParser.END_TAG:
//                        if (tagname.equalsIgnoreCase("employee")) {
//                            // add employee object to list
//                            employees.add(employee);
//                        } else if (tagname.equalsIgnoreCase("name")) {
//                            employee.setName(text);
//                        } else if (tagname.equalsIgnoreCase("id")) {
//                            employee.setId(Integer.parseInt(text));
//                        } else if (tagname.equalsIgnoreCase("department")) {
//                            employee.setDepartment(text);
//                        } else if (tagname.equalsIgnoreCase("email")) {
//                            employee.setEmail(text);
//                        } else if (tagname.equalsIgnoreCase("type")) {
//                            employee.setType(text);
//                        }
//                        break;
//
//                    default:
//                        break;
//                }
//                eventType = parser.next();
//            }
//
//        } catch (XmlPullParserException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        return employees;
//    }
//}
//
