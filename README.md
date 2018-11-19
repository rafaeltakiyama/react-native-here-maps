#  react-native-here-maps (Sample)

## :arrow_up: How to Setup

**Step 1:** git clone this repo:

**Step 2:** cd to the cloned repo:

**Step 3:** Install the Application with `npm i`


## :arrow_forward: How to Run App

1. cd to the repo
2. Run Build for either OS
  * for Android
    * Run Genymotion/Device
    * run `react-native run-android`

## Here Maps Keys

Create this file in android folder:
heremaps.gradle

```
ext {
    heremap_appid ='YOUR_HERE_MAP_APP_ID'
    heremap_apptoken = 'YOUR_HERE_MAP_APP_TOKEN'
}
```

## Configure Here Maps APP ID and APP TOKEN
https://developer.here.com

In here maps website, you need to configure your Android Package name, and when you create your android project you will need to use the same Package name configure in here maps.

## How open Here Maps module
```javascript
//Latitude, Longitude, Zoom, Title, Description (if title is null, the description doesnt appear in maps marker)
HereMapsModule.openHereMaps(-20.5375968, -47.3830002, 17, 'My Title', 'My description')
```