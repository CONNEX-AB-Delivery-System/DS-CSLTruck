# Delivery System / CSL Truck project with Gradle

## About this project

This repository stores a template project about `CSL Truck`. You will use this project to get started and will add your developed software to this project. 

## Getting Started

To develop software you need following things: 
- Development tools installed as per instructions (IntelliJIDEA and Git/GitHub/SourceTree are must, Gradle is optional).
- Learn (reading provided documentation) about capabilities of motors and sensors and how you can control them with your Java code. See section: Documenatation of system for links to this documentation and to see different code examples. 

To run software you need following things: 
- Your truck (this is where your software will execute - not on your computer!) 
- One computer per team with all development tools installed (here Gradle is necessary as well). 

## How to run your code on truck (when you have access to truck in Lab)

The project includes latest dependencies and an example ready to be deployed on Delivery Truck using the `CSL Truck` library from `CONNEX-AB-Delivery-System`. The project includes some tasks to reduce the time to deploy on your robot.

Steps to connect to Truck: 
1) Switch on Truck and wait for OS to load
2) Check the IP of Truck (by default for this truck it should be 192.168.122.223 - if not, update the file `config.gradle`):

```
remotes {
    ev3dev {
        host = '192.168.122.223'
        user = 'robot'
        password = 'maker'
    }
}
```

3) Open Terminal or Command Line. Locate to Project file folder (use commands cd <project directory>). 
4) Connect your computer network to BTH (you don't need to login into this network, just connect). 
5) Now you can use the Java IDE to launch the task or execute them from the terminal

```
./gradlew deployAndRun
```

5b) Some other tasks associated to deploy on your robot are:

- deploy (The project deliver a FatJar to your Brick)
- remoteRun (Execute a jar deployed on your Brick)
- deployAndRun (Deploy & Execute from your Computer the program that you configured on the file: MANIFEST.MF)

# About CSLTruck

motors
- [one EV3 Large Motor] for driving to run front wheel drive system. Documentation here: <a href="http://ev3dev-lang-java.github.io/docs/api/latest/ev3dev-lang-java/ev3dev/actuators/lego/motors/EV3LargeRegulatedMotor.html">Javadocs</a>
- [one EV3 Medium Motor] for steering to run back wheel steering system. Documentation here: <a href="http://ev3dev-lang-java.github.io/docs/api/latest/ev3dev-lang-java/ev3dev/actuators/lego/motors/EV3MediumRegulatedMotor.html">Javadocs</a>
- [one EV3 Large Motor] for lifting crane. Documentation here: <a href="http://ev3dev-lang-java.github.io/docs/api/latest/ev3dev-lang-java/ev3dev/actuators/lego/motors/EV3LargeRegulatedMotor.html">Javadocs</a>
- [one EV3 Medium Motor] for grabbing container. Documentation here: <a href="http://ev3dev-lang-java.github.io/docs/api/latest/ev3dev-lang-java/ev3dev/actuators/lego/motors/EV3MediumRegulatedMotor.html">Javadocs</a>


sensors
- [one EV3 Infrared Sensor] for measuring distance to detect obstacles. Documentation here: <a href="http://ev3dev-lang-java.github.io/docs/api/latest/ev3dev-lang-java/ev3dev/sensors/ev3/EV3IRSensor.html">Javadocs</a> and <a href="http://docs.ev3dev.org/projects/lego-linux-drivers/en/ev3dev-jessie/sensor_data.html#lego-ev3-ir">Sensor capabilities</a>
- [one mindstormsLineReaderV2 Sensor Line] for measuring line reflection to follow the line. Documentation here: <a href="https://github.com/CONNEX-AB-Delivery-System/DS-CSLTruck/blob/master/src/main/java/base/LineReaderV2.java">Javadocs</a> and <a href="http://docs.ev3dev.org/projects/lego-linux-drivers/en/ev3dev-jessie/sensor_data.html#ms-line-leader">Sensor capabilities</a>


# Documenatation of system

## General information

LEGO brick is running on Debian-based operating system ev3dev: https://github.com/ev3dev (for more info see ev3dev links).

And is programmed in JAVA: http://ev3dev-lang-java.github.io/#/. JAVA programms are deployed on brick by using Gradle,
to see how it is done, follow this link: http://ev3dev-lang-java.github.io/docs/support/getting_started/create-your-first-project.html.

## Examples

Exist several examples ready to use here:

https://github.com/ev3dev-lang-java/examples

Another Git repo for example source code available here: https://github.com/ev3dev-lang-java/template_project_gradle).

In order to modify examples, current full APIs are:

http://ev3dev-lang-java.github.io/docs/api/latest/index.html

You mostly will use EV3 Sensors in package: ev3dev.sensors.ev3 <br />
And classes: EV3ColorSensor, EV3IRSensor, EV3TouchSensor, EV3UltrasonicSensor <br />
*note: we also will use custom LineReaderV2 class, documentation here: TODO: LINK <br />


You mostly will use EV3 Motors in package: ev3dev.actuators.lego.motors <br />
And classes: EV3LargeRegulatedMotor, EV3MediumRegulatedMotor

## Sensors and Motors

For ev3dev OS capabilities on EV3Brick and BrickPI3, you can read here: http://docs.ev3dev.org/en/ev3dev-jessie/

You can learn about sensor capabilities here: http://docs.ev3dev.org/projects/lego-linux-drivers/en/ev3dev-jessie/sensor_data.html

You can learn about sensor capabilities here: http://docs.ev3dev.org/projects/lego-linux-drivers/en/ev3dev-jessie/motor_data.html

Hint: value0 -> value(0) in Java.

## Network

If necessary, to set-up wifi, you can access robot through ssh and then use "connman", described here (Section: Connecting to an open access point):  https://wiki.archlinux.org/index.php/ConnMan#Connecting_to_an_open_access_point
