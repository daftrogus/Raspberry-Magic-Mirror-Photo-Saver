package com.example.photo_saver;

import android.os.AsyncTask;
import android.util.Log;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import static android.provider.Settings.System.getString;

public class UploadImageTask extends AsyncTask<File, Integer, Boolean> {

    //upload file to this url
    private static final String UPLOAD_URL = "http://daftrogus.com/index.php"; // JANGAN LUPA FAKING ////

    //send file with this form name
    private static final String FIELD_FILE = "thisisfile";

    //prepare activity before upload
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
//        setProgressBarIndeterminateVisibility(true);
//        mConfirm.setEnabled(false);
//        mCancel.setEnabled(false);
//        showDialog(UPLOAD_PROGRESS_DIALOG);
    }

    /**
     * Clean app state after upload is completed
     */
    @Override
    protected void onPostExecute(Boolean result) {
        super.onPostExecute(result);
//        setProgressBarIndeterminateVisibility(false);
//        mConfirm.setEnabled(true);
//        mDialog.dismiss();
//
//        if (result) {
//            showDialog(UPLOAD_SUCCESS_DIALOG);
//        } else {
//            showDialog(UPLOAD_ERROR_DIALOG);
//        }
    }

    @Override
    protected Boolean doInBackground(File... files) {
        Log.i("Uploader", "Uploading file...");
        Log.i("Uploader", files.toString());
        return doFileUpload(files[0], UPLOAD_URL);
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);

//        if (values[0] == 0) {
//            mDialog.setTitle(getString(R.string.progress_dialog_title_uploading));
//        }
//
//        mDialog.setProgress(values[0]);
    }

    /**
     * Upload given file to given url, using raw socket
     //* @see http://stackoverflow.com/questions/4966910/androidhow-to-upload-mp3-file-to-http-server
     *
     * @param file The file to upload
     * @param uploadUrl The uri the file is to be uploaded
     *
     * @return boolean true is the upload succeeded
     */
    private boolean doFileUpload(File file, String uploadUrl) {
        HttpURLConnection conn = null;
        DataOutputStream dos = null;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        String separator = twoHyphens + boundary + lineEnd;
        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024;
        int sentBytes = 0;
        long fileSize = file.length();

        // The definitive url is of the kind:
        // http://host/report/latitude,longitude
//        uploadUrl += "/" + mLocation.getLatitude() + "," + mLocation.getLongitude();

        // Send request
        try {
            // Configure connection
            URL url = new URL(uploadUrl);
            conn = (HttpURLConnection) url.openConnection();
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
            publishProgress(0);

            dos = new DataOutputStream(conn.getOutputStream());

            // Send location params
//            writeFormField(dos, separator, FIELD_LATITUDE, "" + mLocation.getLatitude());
//            writeFormField(dos, separator, FIELD_LONGITUDE, "" + mLocation.getLongitude());
            // Ini samain sama phpnya, di php kan ada $_FILES['nama_parameter']
            // Harus sama sama tu FIELD_FILE
            // Send multipart headers
            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Disposition: form-data; name=\"thisisfile\";filename=\""
                    + file.getName() + "\"" + lineEnd);
            dos.writeBytes(lineEnd);

            // Read file and create buffer
            FileInputStream fileInputStream = new FileInputStream(file);
            bytesAvailable = fileInputStream.available();
            bufferSize = Math.min(bytesAvailable, maxBufferSize);
            buffer = new byte[bufferSize];

            // Send file data
            bytesRead = fileInputStream.read(buffer, 0, bufferSize);
            while (bytesRead > 0) {
                Log.i("Uploader", "Uploading: "+bytesRead);
                // Write buffer to socket
                dos.write(buffer, 0, bufferSize);

                // Update progress dialog
                sentBytes += bufferSize;
                publishProgress((int)(sentBytes * 100 / fileSize));

                bytesAvailable = fileInputStream.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);
            }

            // send multipart form data necesssary after file data
            dos.writeBytes(lineEnd);
            dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
            dos.flush();
            dos.close();
            fileInputStream.close();
            Log.i("Uploader", "Upload Complete");
        } catch (IOException ioe) {
            Log.e("TAG", "Cannot upload file: " + ioe.getMessage(), ioe);
            return false;
        }

        // Read response
        try {
            int responseCode = conn.getResponseCode();
            String reply;
            InputStream in = conn.getInputStream();
            StringBuffer sb = new StringBuffer();
            try {
                int chr;
                while ((chr = in.read()) != -1) {
                    sb.append((char) chr);
                }
                reply = sb.toString();
            } finally {
                in.close();
            }
            Log.i("Uploader", reply);
            return responseCode == 200;
        } catch (IOException ioex) {
            Log.e("TAG", "Upload file failed: " + ioex.getMessage(), ioex);
            return false;
        } catch (Exception e) {
            Log.e("TAG", "Upload file failed: " + e.getMessage(), e);
            return false;
        }
    }

    private void writeFormField(DataOutputStream dos, String separator, String fieldName, String fieldValue) throws IOException
    {
        dos.writeBytes(separator);
        dos.writeBytes("Content-Disposition: form-data; name=\"" + fieldName + "\"\r\n");
        dos.writeBytes("\r\n");
        dos.writeBytes(fieldValue);
        dos.writeBytes("\r\n");
    }
}
