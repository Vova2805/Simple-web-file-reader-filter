#This is a simple WEB ServletAPI for filtering/querying text files.

[Quick view]()
For starting with it perform following steps:
    1. Configure Tomcat 8.x
    2. Deploy GL_task.war file
On welcome page ("/") you will have few choices - its are intro demo.
For using API go to (“/api/”)
Using this API you can:
    1. Read every file, placed inside resources folder.
    2. Filter file content
Acceptable query parameters:
    1. limit - integer value which represents max number of chars in text that API should return.
    2. q - string which represents text to search in file.
    3. length - integer which represents max string length.
    4. includeMetaData - boolean which if set to true will expose file metadata in API response
       alongside textual content.
    5. useNIO - boolean which if set to true configure API to use NIO objects for reading file (high performance).
If file path or query parameter is wrong you will see error page.


