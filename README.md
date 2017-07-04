# line-bot-java-demo

https://github.com/line/line-bot-sdk-java

- PWS
```
$ ./gradlew clean build 

$ cf login -a api.run.pivotal.io
$ cf push
```

- Heroku
```
$ heroku login
$ heroku create line-bot-java-demo-hirakida
$ git push heroku master
```

Add the following values to Environment Variables.  
```
line.bot.channel-token 
line.bot.channel-secret
```
