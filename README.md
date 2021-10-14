# Opening Hours formatter

This is the service receives restaurant schedule in json format and returns it in human-readable format using a 12-hour clock.

Assumptions
1. If the earliest event of the day is "close" then we assume that event is from previous day.
2. Events are not overlaying
3. Duplicate events(same type and value) are ignored
4. If there is only one open event one day and no close event next day then we throw an exception 
   (case when restaurant opens for example on monday and closes on wednesday is not supported)


## Build and execute the project
### Executable .jar file
If you want to build the project, you need to execute the following command:

```bash
mvn clean install
```
You can then run the service by executing the following command:
```bash
mvn spring-boot:run 
```
You can execute following command to run code checks:
```bash
mvn clean install -PcodeChecks
```

## Docker Container
This project is also runnable as a Docker container.
If you want to build the corresponding image, you need to execute the following in the project's root folder:

Build the image
```bash
docker build -t opening-hours . 
```
You can then run the service by executing the following command
```bash
docker run -p 8088:8088 opening-hours
```

## Calling the service
You can access the service by sending POST request to `http://localhost:8088/restaurants/opening-hours`
using the following example payload 
```json
{
  "monday": [
    {
      "type": "open",
      "value": 28800
    },
    {
      "type": "close",
      "value": 72000
    }
  ],
  "tuesday": [],
  "wednesday": [],
  "thursday": [],
  "friday": [],
  "saturday": [],
  "sunday": []
}
```

## Further development

Following steps can be considered as possible improvement/deployment preparation:

1. Case when restaurant works from monday to friday 24h and closed during weekends
2. Before deployment if there are any interactions with other services I would write some functional tests(using karate)
3. Pipeline integration, codeChecks maven profile would be useful to be executed before PR completion or project release
4. Monitoring tools integration is also would be necessary improvement before going to production

## Thoughts about json format

Current structure is sufficient, however I am not sure if the empty array for a day of the week is necessary here since 
we dedicate a separate filed to each day. 
If we had a json array of days then empty events array is necessary to keep the order, 
it would let us treat every day as same object and iterate over them.