package nami.apps.dbsqlxmlconverter.model;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import androidx.core.content.FileProvider;

import java.io.File;
import java.io.FileOutputStream;

import nami.apps.dbsqlxmlconverter.contract.XmlResultContract;
import nami.apps.dbsqlxmlconverter.model.fileManager.FileProperties;
import nami.apps.dbsqlxmlconverter.model.xml.XML_File;

public class XmlResultModel implements XmlResultContract.Model {

    private String XML_FILE_NAME = "XML_";
    private String XML_EXT = ".xml";
    private File file;
    private Context context;
    private FileProperties fileProperties;
    private String xmlResult;
    @Override
    public void getXMLResult(final OnListerner listerner, Context context, FileProperties fileProperties) {
        File dbFile = new File(fileProperties.getPath());

        this.context = context;
        this.fileProperties = fileProperties;

        final XML_File xml_file = new XML_File(context, dbFile);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    xmlResult = xml_file.convertToXML();
                    listerner.onXMLResultSuccess(xmlResult);
                }
                catch (Exception e)
                {
                    listerner.onFailure("Unable to convert the file");
                }
            }
        }).start();
    }

    @Override
    public void openFile(OnListerner listerner) {
        try {

            String dataType = "";

            if (file.toString().contains(".doc") || file.toString().contains(".docx")) {
                // Word document
                dataType ="application/msword";
            } else if (file.toString().contains(".pdf")) {
                // PDF file
                dataType = "application/pdf";
            } else if (file.toString().contains(".ppt") || file.toString().contains(".pptx")) {
                // Powerpoint file
                dataType = "application/vnd.ms-powerpoint";
            } else if (file.toString().contains(".xls") || file.toString().contains(".xlsx")) {
                // Excel file
                dataType = "application/vnd.ms-excel";
            } else if (file.toString().contains(".zip")) {
                // ZIP file
                dataType = "application/zip";
            } else if (file.toString().contains(".rar")){
                // RAR file
                dataType = "application/x-rar-compressed";
            } else if (file.toString().contains(".rtf")) {
                // RTF file
                dataType =  "application/rtf";
            } else if (file.toString().contains(".wav") || file.toString().contains(".mp3")) {
                // WAV audio file
                dataType = "audio/x-wav";
            }
            else if (file.toString().contains(".xml") || file.toString().contains(".XML")) {
                // WAV audio file
                dataType = "text/xml";
            }else if (file.toString().contains(".gif")) {
                // GIF file
                dataType =  "image/gif";
            } else if (file.toString().contains(".jpg") || file.toString().contains(".jpeg") || file.toString().contains(".png")) {
                // JPG file
                dataType = "image/jpeg";
            } else if (file.toString().contains(".txt")) {
                // Text file
                dataType = "text/plain";
            } else if (file.toString().contains(".3gp") || file.toString().contains(".mpg") ||
                    file.toString().contains(".mpeg") || file.toString().contains(".mpe") ||
                    file.toString().contains(".mp4") || file.toString().contains(".avi")) {
                // Video files
                dataType = "video/*";
            }
            else {
                dataType =  "*/*";
            }

            listerner.onOpenFileSuccess(file,dataType);

        } catch (ActivityNotFoundException e) {
                listerner.onFailure("Exception: " + e);
           }
    }

    @Override
    public void saveFile(OnListerner listerner) {
        //Create a new file that points to the root directory, with the given name:
        String name = fileProperties.getName().substring(0,fileProperties.getName().length()-3);
        file = new File(this.context.getExternalFilesDir(null), XML_FILE_NAME + name + XML_EXT );

        //This point and below is responsible for the write operation
        FileOutputStream outputStream = null;
        try {
            file.createNewFile();
            //second argument of FileOutputStream constructor indicates whether
            //to append or create new file if one exists
            outputStream = new FileOutputStream(file, false);

            outputStream.write(xmlResult.getBytes());
            outputStream.flush();
            outputStream.close();

            listerner.onSaveFileSuccess("The contents are saved in the ." + XML_FILE_NAME + name + XML_EXT);

        } catch (Exception e) {
         listerner.onFailure("Exception: " + e);
            e.printStackTrace();
        }
    }
}
