# Velocity

An easy to use networking library for Android

# Features
- Simplified networking
- Supports GET/POST/PUT/DELETE (for now)
- Supports file downloads and uploads (with progress callbacks)
- Supports http post data (json, multipart, url-encoded, plain text, xml)
- Simple OAuth logins using password resource flow
- Queued requests
- Global and per request mocking
- Built in deserialization for JSON (using gson)
- Handles redirects
- Adjustable settings : read/request timeout, mock delay, global simulated delay, max redirects, user agent, multipart boundary


1) Installation
Add the following to your project's main gradle file:
```gradle
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
``` 
Add the following to your app.gradle file
```gradle
    compile 'com.github.ravindu1024:velocity:1.0.1'
    compile 'com.google.code.gson:gson:2.7'
```

# Usage

Velocity uses a background threadpool to execute network requests and it needs to initialized at app launch:
```java
    Velocity.initialize(3);     //initialize the threadpool with 3 background threads. All threads will be waiting on a queue.
```

Simple GET request:
```java
Velocity.get("http://www.google.com").connect(new Velocity.ResponseListener()
        {
            @Override
            public void onVelocitySuccess(Velocity.Response response)
            {
            }

            @Override
            public void onVelocityFailed(Velocity.Response error)
            {
            }
        });
```

GET request with headers and path parameters:
```java
Velocity.get(url)
                .withHeader("header1", "value1")
                .withHeader("header2", "value2")
                .withPathParam("path1", "value1")
                .withPathParam("path2", "value2")
                .connect(new Velocity.ResponseListener()
                {
                    @Override
                    public void onVelocitySuccess(Velocity.Response response)
                    {
                    }

                    @Override
                    public void onVelocityFailed(Velocity.Response error)
                    {
                    }
                });
```

POST with form data:
```java
Velocity.post(url)
                .withFormData("key1", "value1")
                .withFormData("key2", "value2")
                .connect(new Velocity.ResponseListener()
                {
                    @Override
                    public void onVelocitySuccess(Velocity.Response response)
                    {
                    }

                    @Override
                    public void onVelocityFailed(Velocity.Response error)
                    {
                    }
                });
```

Queing multiple requests:
```java
        Velocity.get(url).queue(0);
        Velocity.download(file).setDownloadFile(filepath).queue(1);
        Velocity.get(randomImage).queue(2);

        Velocity.executeQueue(new Velocity.MultiResponseListener()
        {
            @Override
            public void onVelocityMultiResponseSuccess(HashMap<Integer, Velocity.Response> responseMap)
            {
            }

            @Override
            public void onVelocityMultiResponseError(HashMap<Integer, Velocity.Response> errorMap)
            {
            }
        });
```

Deserialization:
```java
MyWrapperClass data = serverResponse.deserialize(MyWrapperClass.class);
```

