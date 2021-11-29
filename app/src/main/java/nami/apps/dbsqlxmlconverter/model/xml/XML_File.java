package nami.apps.dbsqlxmlconverter.model.xml;

import android.content.Context;
import android.util.Log;
import android.util.Xml;
import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;

import nami.apps.dbsqlxmlconverter.model.database.AnyDB_Controller;

public class XML_File {

    private final Context context;
    private final File file;
    private ArrayList<String> lisTableName;
    private ArrayList<String> listDatainAColumn;
    private ArrayList<String> listColumnNames;

    public XML_File(Context context, File file) {
        this.context = context;
        this.file = file;
    }

    public  String convertToXML()  throws IllegalArgumentException, IllegalStateException, IOException {

        try{
            XmlSerializer xmlSerializer = Xml.newSerializer();
            StringWriter writer = new StringWriter();
            xmlSerializer.setOutput(writer);

            //Start Document
            xmlSerializer.startDocument("UTF-8", true);


            AnyDB_Controller anyDB_controller = new AnyDB_Controller(this.context, this.file);

            lisTableName = anyDB_controller.getAllTableName();

            //todo - re do the xml format. make sure correct

       /* what we have now
       <pkg_info>
            <InfoName>Name_1</InfoName>
            <InfoValue>1</InfoValue>
             <InfoName>Name_2</InfoName>
            <InfoValue>2</InfoValue>
             <InfoName>Name_2</InfoName>
            <InfoValue>2</InfoValue>
        </pkg_info>
        */

       /*
            suppose to be
 <root>
    <table_1>
	    <PKG_INFO>
		    <InfoName>Name_1</InfoName>
		    <InfoValue>1</InfoValue>
	    </PKG_INFO>
	    <PKG_INFO>
		    <InfoName>Name_2</InfoName>
		    <InfoValue>2</InfoValue>
	    </PKG_INFO><PKG_INFO>
	    	<InfoName>Name_3</InfoName>
		    <InfoValue>3</InfoValue>
	    </PKG_INFO>
    </table_1>
    <table_2>
	    <android_metadata>
		    <locale>en_US</locale>
	    </android_metadata>
    </table_2>
</root>
       */

            xmlSerializer.startTag(null,"root");

            for(int i=0;i<lisTableName.size() ; i++)
            {
                xmlSerializer.startTag(null,"table_" + i);

                int row = anyDB_controller.getRowCount(lisTableName.get(i));
                if(row!=0)
                {
                    listColumnNames = anyDB_controller.getAllColumnNamesbyTableName(lisTableName.get(i));
                    if(listColumnNames.size()!=0)
                    {
                        if(row>5000)
                            row=5000;

                        for (int j =1 ;j<= row ; j++)
                        {
                            xmlSerializer.startTag(null,lisTableName.get(i));

                            for (String columnName : listColumnNames) {
                                String data = anyDB_controller.getDataFromTableByColumnAndRow(lisTableName.get(i), columnName, j);
                                if(data!=null)
                                {
                                    if (data.equals("")){
                                        xmlSerializer.startTag(null, columnName);
                                        xmlSerializer.endTag(null, columnName);
                                    } else {
                                        xmlSerializer.startTag(null, columnName);
                                        xmlSerializer.text(data);
                                        xmlSerializer.endTag(null, columnName);
                                    }
                                }
                                else {
                                    xmlSerializer.startTag(null, columnName);
                                    xmlSerializer.endTag(null, columnName);
                                }
                            }

                            xmlSerializer.endTag(null,lisTableName.get(i));
                        }
                    }
                }
                else {
                    xmlSerializer.startTag(null,lisTableName.get(i));
                    xmlSerializer.endTag(null,lisTableName.get(i));
                }

                xmlSerializer.endTag(null,"table_" + i);
            }
            xmlSerializer.endTag(null,"root");

            xmlSerializer.endDocument();

            return writer.toString();
        }
        catch (Exception e)
        {
            String error = "Unable to convert the file : " + e;
            Log.e(getClass().getName(),error);
            return error;
        }

    }

}
