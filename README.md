# line-bot-java-demo

https://github.com/line/line-bot-sdk-java

- Cloud Foundry
```
$ ./gradlew clean build 

# Pivotal Web Services
$ cf login -a api.run.pivotal.io
# IBM Bluemix
$ cf login -a api.ng.bluemix.net

$ cf push
```

Add the following values to Environment Variables.  
```
line.bot.channel-token 
line.bot.channel-secret
```
