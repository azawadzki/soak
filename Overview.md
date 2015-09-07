soak is a tiny library intended for accessing data stored in SpiderOak cloud. It uses SpiderOak Web API internally and wraps it in Java API.

The library is written in Java (tested against version 6) and depends on  	json-simple library (tested against 1.1).

Using soak you can use almost all functions of SpiderOak Web API, apart from accessing older versions of files.

Currently, in order to access all elements, you always need to start from top-level elements and get down for your sought file or dir on your own. Accessing random files using string with filepath may get added in next library version.

The code below demonstrates how can you use the library:
```
// You need to create objects which handle login and data transfer first
AccountInfo accountInfo = new AccountInfo(userName, password);
ConnectionHandler conn = new ConnectionHandler(accountInfo);

// Afterwards you can access your data by instantiating Shares and Storage
// classes and iterating over their contents
Shares shares = new Shares(accountInfo, conn);
// keep in mind that you must call loadData() always before accessing
// objects data other than name or URL!
shares.loadData();

// here we iterate over directories stored in user shares
for (ShareRoom r: shares.getShareRooms()) {
  r.loadData();
  for (Dir d: r.getDirs()) {
    // do something interesting with the data...
  }
}

// The code below shows how one could download files from SpiderOak storage.
Storage storage = new Storage(accountInfo, conn);
storage.loadData();

// looking for some files for download...
for (Dir d1: storage.getDevices()) {
  d1.loadData();
  for (Dir d2: d1.getDirs()) {
    d2.loadData();
    for (File f: d2.getFiles()) {
      InputStream inStream = conn.getDownloadStream(f.getUrl());
      // read from inStream as you'd normally do.
    }
  }
}
```
For more information consult javadoc.