import com.sun.istack.internal.NotNull;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainJFrame extends JFrame
{

    @NotNull private IController controller = new DefectsController();
    @NotNull private TabbedPane tabbedPane = new TabbedPane();

    @NotNull
    private JFileChooser fileChooser = new JFileChooser();
    private DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
    private File lastDirectory;

    /*private Predicate<IDefectInfoProvider> getValidator(String versionString)
    {
        if (versionString.equals("all")) {
            return (defectInfo -> true);
        } else {
            return (defectInfo -> defectInfo.getReportedVersion().equals(versionString));
        }
    }*/

    public MainJFrame()
    {
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        JFrame thisFrame = this;
        addWindowListener(new WindowAdapter()
        {
            public void windowClosing(WindowEvent e)
            {
                int result = JOptionPane.showConfirmDialog(thisFrame, "Save current data?");
                try {
                    switch (result) {
                        case 2:
                            return;
                        case 0:
                            saveToXml();
                        default:
                            thisFrame.dispose();
                    }
                }catch (Exception ex)
                {
                    System.out.println("Error:" + ex.getMessage());
                }
            }
        });
        FileNameExtensionFilter filter = new FileNameExtensionFilter("xml files", "xml");
        fileChooser.setFileFilter(filter);
        setMinimumSize(new Dimension(1130, 500));
        tabbedPane.addTab("All", controller::getDefectInfoSublist, new Filter(""), controller::changeDefect);

        JPanel buttonPanel = new JPanel();
        BoxLayout buttonPanelLayout = new BoxLayout(buttonPanel, BoxLayout.LINE_AXIS);
        buttonPanel.setLayout(buttonPanelLayout);
        buttonPanel.add(Box.createHorizontalGlue());
        JButton importFileButton = new JButton("Import File");
        buttonPanel.add(importFileButton);
        JButton exportFileButton = new JButton("Export File");
        buttonPanel.add(exportFileButton);
        exportFileButton.addActionListener(this::exportFile);
        importFileButton.addActionListener(this::importFile);
        buttonPanel.add(Box.createHorizontalGlue());
        JButton newDefectButton = new JButton("New Defect");
        buttonPanel.add(newDefectButton);
        newDefectButton.addActionListener(this::openAddDefectFrame);

        this.setPreferredSize(new Dimension(700, 500));
        BoxLayout layout = new BoxLayout(getContentPane(), BoxLayout.PAGE_AXIS);
        getContentPane().setLayout(layout);

        add(tabbedPane);
        add(buttonPanel);

        pack();
    }

    private void openAddDefectFrame(ActionEvent e)
    {
        DefectInfoJFrame defectInfoJFrame = DefectInfoJFrame.getAddDefectFrame(this::createNewDefect);
        defectInfoJFrame.setVisible(true);
    }

    private void createNewDefect(@NotNull IDefectInfoProvider defectInfo)
    {
        controller.addDefect(defectInfo);
        ((TabPanel) tabbedPane.getSelectedComponent()).addDefectLineIfFit(defectInfo);
    }

    private void exportFile(ActionEvent e)
    {
        try {
            saveToXml();
        } catch (Exception ex) {
            System.out.println("Error:" + ex.getMessage());
        }

    }

    private void importFile(ActionEvent e)
    {
        //todo cheeki breeki
        try {
            int result = JOptionPane.showConfirmDialog(this, "Save current data?");

            switch (result) {
                //cancel button was clicked - do nothing
                case 2:
                    return;
                // "yes" button was clicked - save data, then load data
                case 0:
                    saveToXml();
            }
            // if "no" or "yes" button was clicked - load data
            loadFromXml();
        } catch (Exception ex) {
            System.out.println("Error:" + ex.getMessage());
        }
    }


    private void cleanAllData()
    {
        tabbedPane.removeAll();
        controller.cleanData();
    }

    //todo cheeki breeki
    //todo cheeki breeki
    private void saveToXml() throws Exception
    {
        Document doc;
        try {
            DocumentBuilder db = dbf.newDocumentBuilder();
            doc = db.newDocument();
            Element rootElement = doc.createElement("DefectNoteManagerData");
            //writing tabs
            Element tabs = doc.createElement("Tabs");
            Element e;
            for (int i = 0; i < tabbedPane.getTabCount(); i++) {
                e = ((TabPanel) tabbedPane.getComponentAt(i)).getXmlElement(doc);
                tabs.appendChild(e);
            }
            rootElement.appendChild(tabs);
            //writing defects
            Element defects = doc.createElement("Defects");
            controller.getDefectInfoList().forEach(defectInfo -> defects.appendChild(defectInfo.getXmlElement(doc)));
            rootElement.appendChild(defects);
            doc.appendChild(rootElement);

            Transformer tr = TransformerFactory.newInstance().newTransformer();
            if (lastDirectory != null) {
                fileChooser.setCurrentDirectory(lastDirectory);
            }

            //todo cheeki breeki
            if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                if (!file.getAbsolutePath().endsWith(".xml")) {
                    file = new File(file.getAbsolutePath().concat(".xml"));
                }
                lastDirectory = file;
                tr.transform(new DOMSource(doc), new StreamResult(file));
            }

        } catch (TransformerException te) {
            System.out.println(te.getMessage());
        } catch (ParserConfigurationException pce) {
            System.out.println("UsersXML: Error trying to instantiate DocumentBuilder " + pce);
        }
    }

    private void loadFromXml()
    {
        if (lastDirectory != null) {
            fileChooser.setCurrentDirectory(lastDirectory);
        }
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            cleanAllData();
            File file = fileChooser.getSelectedFile();
            lastDirectory = file;
            try {
                DocumentBuilder db = dbf.newDocumentBuilder();
                Document doc = db.parse(file);
                doc.getDocumentElement().normalize();
                NodeList tabsElement = doc.getElementsByTagName("Tabs");
                NodeList defectsElement = doc.getElementsByTagName("Defects");
                Exception incorrectFileException = new Exception("Incorrect xml file");
                if (tabsElement.getLength() != 1 || defectsElement.getLength() != 1) {
                    throw incorrectFileException;
                }
                NodeList defectsElementList = defectsElement.item(0).getChildNodes();
                for (int temp = 0; temp < defectsElementList.getLength(); temp++) {
                    if (defectsElementList.item(temp).getNodeType() == Node.ELEMENT_NODE) {
                        createDefectFromElement((Element) defectsElementList.item(temp));
                    }
                }
                NodeList tabsElementList = tabsElement.item(0).getChildNodes();
                for (int temp = 0; temp < tabsElementList.getLength(); temp++) {
                    if (tabsElementList.item(temp).getNodeType() == Node.ELEMENT_NODE) {
                        createTabFromElement((Element) tabsElementList.item(temp));
                    }
                }


            } catch (ParserConfigurationException | SAXException pce) {
                System.out.println(pce.getMessage());
            } catch (IOException ioe) {
                System.err.println(ioe.getMessage());
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    private void createTabFromElement(Element tabElement) throws Exception
    {
        String tabTitle = tabElement.getAttribute("Title");
        NodeList filters = tabElement.getElementsByTagName("Filter");
        if (filters.getLength() != 1 || filters.item(0).getNodeType() != Node.ELEMENT_NODE) {
            throw new Exception("Incorrect xml file");
        }
        Element filterElement = (Element) filters.item(0);
        Filter tabFilter = new Filter(filterElement.getAttribute("ReportedForVersion"));
        tabbedPane.addTab(tabTitle, controller::getDefectInfoSublist, tabFilter, controller::changeDefect);

    }

    private void createDefectFromElement(Element defectElement) throws Exception
    {
        IDefectInfoProvider defectInfoProvider = new IDefectInfoProvider()
        {
            @Override @NotNull
            public String getDefectName()
            {
                return defectElement.getAttribute("Name");
            }

            @Override @NotNull
            public String getReportedVersion()
            {
                return defectElement.getAttribute("ReportedVersion");
            }

            @Override @NotNull
            public DefectStatesEnum getDefectState()
            {
                return DefectStatesEnum.getFromString(defectElement.getAttribute("State"));
            }

            @Override @NotNull
            public List<IDefectVersionInfo> getVersionInfoList()
            {
                NodeList defectVersions = defectElement.getElementsByTagName("DefectVersion");
                List<IDefectVersionInfo> defectVersionInfos = new ArrayList<>();
                for (int temp = 0; temp < defectVersions.getLength(); temp++) {
                    String version = ((Element) defectVersions.item(temp)).getAttribute("Version");
                    DefectVersionStatesEnum state = DefectVersionStatesEnum.getFromString(((Element) defectVersions.item(temp)).getAttribute("State"));
                    defectVersionInfos.add(new IDefectVersionInfo()
                    {
                        @Override @NotNull
                        public String getVersion()
                        {
                            return version;
                        }

                        @Override @NotNull
                        public DefectVersionStatesEnum getState()
                        {
                            return state;
                        }
                    });
                }
                return defectVersionInfos;

            }

            @Override @NotNull
            public String getComment()
            {
                return defectElement.getAttribute("Comment");
            }
        };

        controller.addDefect(defectInfoProvider);
    }
}
