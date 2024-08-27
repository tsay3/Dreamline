// Version 2.0

package com.example.projectdreamline.data;
public class Data {
  public static String[] statements = {
//          "CREATE SCHEMA sampleFlightDB AUTHORIZATION sa;",
//   "USE sampleFlightDB;",
  "DROP SCHEMA IF EXISTS sampleFlightDB;",
  "CREATE SCHEMA sampleFlightDB AUTHORIZATION sa;",
//  "USE SCHEMA sampleFlightDB;",
  "DROP TABLE flight_comfort IF EXISTS;",
  "DROP TABLE users IF EXISTS;",
  "DROP TABLE flight IF EXISTS;",
  "DROP TABLE airport IF EXISTS;",
  "DROP TABLE airline IF EXISTS;",
  "CREATE TABLE airline (" +
    "carrierCode VARCHAR(3) PRIMARY KEY," +
    "name VARCHAR(32)" +
  ");",

  "INSERT INTO airline values('AA', 'American Airlines');",
  "INSERT INTO airline values('UA', 'United Airlines');",
  "INSERT INTO airline values('DA', 'Delta Airlines');",
  "INSERT INTO airline values('NK', 'Spirit Airlines');",
  "INSERT INTO airline values('SWA', 'Southwest Airlines');",
  "INSERT INTO airline values('UX', 'Air Europa');",

  "CREATE TABLE airport (" +
    "iataCode CHAR(3) PRIMARY KEY," +
    "city VARCHAR(20)," +
    "fullName VARCHAR(50)" +
  ");",

  "INSERT INTO airport values('LAX', 'Los Angeles', 'Los Angeles International Airport');",
  "INSERT INTO airport values('ORD', 'Chicago', 'O''Hare International Airport');",
  "INSERT INTO airport values('MDW', 'Chicago', 'Midway International Airport');",
  "INSERT INTO airport values('JFK', 'New York', 'John F Kennedy International Airport');",
  "INSERT INTO airport values('MAD', 'Madrid', 'Adolfo Suárez Madrid–Barajas Airport');",
  "INSERT INTO airport values('LAS', 'Las Vegas', 'Harry Reid International Airport');",

  "CREATE TABLE flight (" +
    "flight_id INT PRIMARY KEY," +
    "carrierCode VARCHAR(3)," +
    "flight_num SMALLINT," +
    "dept_station_code CHAR(3)," +
    "arr_station_code CHAR(3)," +
    "depart_time TIME," +
    "arrive_time TIME," +
    "price DECIMAL(5,2)," +
    "FOREIGN KEY (carrierCode) REFERENCES airline(carrierCode)," +
    "FOREIGN KEY (dept_station_code) REFERENCES airport(iataCode)," +
    "FOREIGN KEY (arr_station_code) REFERENCES airport(iataCode)" +
  ");",

  "INSERT INTO flight values(1, 'NK', 2804, 'LAX', 'ORD', '23:34:00', '5:45:00', 113.00);",
  "INSERT INTO flight values(2, 'NK', 327, 'LAX', 'LAS', '6:45:00', '8:00:00', 79.00);",
  "INSERT INTO flight values(3, 'NK', 2524, 'LAS', 'ORD', '13:29:00', '19:05:00', 125.00);",

  "CREATE TABLE users (" +
    "user_id INT PRIMARY KEY," +
    "username VARCHAR(40)" +
//    "first_name VARCHAR(30)," +
//    "last_name VARCHAR(30)," +
//    "birth_date DATE," +
//    "email VARCHAR(60)" +
  ");",

  "INSERT INTO users values(1, 'mchav4');",
  "INSERT INTO users values(2, 'hopscotch');",
  "INSERT INTO users values(3, 'mynamehere');",

//  "INSERT INTO users values(1, 'mchav4', 'Miguel', 'Chavez', '1999-05-03', 'mchav4@email.com');",
//  "INSERT INTO users values(2, 'hopscotch', 'Anna', 'Chavez', '2000-07-23', 'pinkie6@email.com');",
//  "INSERT INTO users values(3, 'mynamehere', 'Sally', 'Sample', '1995-01-01', 'ssample@email.com');",

  // Corresponds to the data object Flight_Comfort
  "DROP TABLE flight_comfort IF EXISTS;",
  "CREATE TABLE flight_comfort (" +
    "entry_id INT PRIMARY KEY," +
    "flight_id INT," +
    "user_id INT," +
    "time_submitted DATETIME," +
    "lowest_heart_rate FLOAT," +
    "avg_heart_rate FLOAT," +
    "highest_heart_rate FLOAT," +
    "lowest_oxygen_rate FLOAT," +
    "avg_oxygen_rate FLOAT," +
    "highest_oxygen_rate FLOAT," +
    "lowest_heart_rate_liftoff FLOAT," +
    "avg_heart_rate_liftoff FLOAT," +
    "highest_heart_rate_liftoff FLOAT," +
    "lowest_oxygen_rate_liftoff FLOAT," +
    "avg_oxygen_rate_liftoff FLOAT," +
    "highest_oxygen_rate_liftoff FLOAT," +
    "lowest_heart_rate_landing FLOAT," +
    "avg_heart_rate_landing FLOAT," +
    "highest_heart_rate_landing FLOAT," +
    "lowest_oxygen_rate_landing FLOAT," +
    "avg_oxygen_rate_landing FLOAT," +
    "highest_oxygen_rate_landing FLOAT," +
    "customer_review TINYINT," +
    "FOREIGN KEY (flight_id) REFERENCES flight(flight_id)," +
    "FOREIGN KEY (user_id) REFERENCES users(user_id)" +
  ");",

  "INSERT INTO flight_comfort values(1, 1, 1, '2020-05-03 5:45:00'," +
    "75.13860214175698, 85.67482733230707, 93.53078848843123," +
    "92.60495388350002, 95.1626465409858, 98.57583601744294," +
    "77.25443622729793, 85.81497575959922, 93.53078848843123," +
    "92.60495388350002, 95.59039495047148, 98.57583601744294," +
    "79.78284804581328, 87.0644130946621, 92.82068577306701," +
    "93.03380306431127, 95.6359605999202, 98.23811813552913, 5);",
  "INSERT INTO flight_comfort values(2, 1, 2, '2020-05-03 5:45:00'," +
    "65.5271299262982, 76.4880692069983, 83.55736513862445," +
    "90.11151221310402, 94.23451032119267, 98.32372215375007," +
    "70.11548422535623, 75.86319832096734, 79.8001602700635," +
    "90.52918307291563, 94.29077736709212, 98.05237166126861," +
    "69.59063420572816, 77.33516637561657, 83.12688705489184," +
    "90.42421306899001, 94.37396761137003, 98.32372215375007, 5);",
  "INSERT INTO flight_comfort values(3, 1, 3, '2020-05-03 5:45:00'," +
    "89.11388674641744, 97.89902368765684, 107.60007857323141," +
    "94.41477628697169, 96.80696441261384, 98.69735530621023," +
    "92.51120580757532, 100.15812203407366, 107.60007857323141," +
    "94.59424009920326, 96.47335712866953, 98.35247415813582," +
    "92.18067012816658, 99.94885319013379, 106.85570201633548," +
    "94.52813296332151, 96.39083171097177, 98.25353045862202, 5);",
  "INSERT INTO flight_comfort values(4, 2, 1, '2020-05-04 8:00:00'," +
    "81.32704230895476, 100.78279681854858, 115.32766572255673," +
    "93.01868006664256, 96.06862138255768, 99.25770833029938," +
    "84.07681755909475, 91.91557736423675, 98.00397439489642," +
    "93.06863511667056, 95.99527892813104, 98.92192273959152," +
    "82.528526806076, 99.11757834744596, 114.8763864845959," +
    "93.01868006664256, 96.13819419847097, 99.25770833029938, 5);",
  "INSERT INTO flight_comfort values(5, 2, 2, '2020-05-04 8:00:00'," +
    "87.40270902186782, 101.75318227735106, 114.43527291025033," +
    "90.33279666147537, 95.29939746686694, 99.46779660005438," +
    "92.04644166685851, 99.97080744775646, 104.95004796244868," +
    "90.7615431904735, 95.11466989526394, 99.46779660005438," +
    "92.09552198814265, 103.948383408347, 113.88265930869295," +
    "90.77135925473033, 95.08982008313029, 99.40828091153027, 5);",
    "INSERT INTO flight_comfort values(6, 2, 3, '2020-05-04 8:00:00'," +
      "79.41897437973329, 96.72345351317827, 111.45355916645458," +
      "90.42112089374845, 94.69991535683332, 98.38772271372756," +
      "81.88027285874026, 96.90240542938086, 111.45355916645458," +
      "90.42112089374845, 94.404421803738, 98.38772271372756," +
      "83.14341671400098, 89.42284655803005, 94.15325222105699," +
      "90.66600936060199, 94.52686603716478, 98.38772271372756, 5);",
    "INSERT INTO flight_comfort values(7, 3, 1, '2020-05-05 19:05:00'," +
      "75.60994926732091, 88.89201259859969, 104.25164836892002," +
      "92.94174213444659, 95.8049467010242, 99.35917922952761," +
      "77.09949770793128, 91.10487302467205, 104.25164836892002," +
      "92.94174213444659, 95.9937222000565, 99.0457022656664," +
      "80.2157403213547, 93.63590182646793, 103.43122750696311," +
      "93.36290034525334, 96.19727081837172, 99.03164129149012, 5);",
    "INSERT INTO flight_comfort values(8, 3, 2, '2020-05-05 19:05:00'," +
      "89.55940475824528, 106.71944716752344, 116.84498979981964," +
      "90.20369223947048, 94.89017454570902, 99.32949365245918," +
      "93.37589191061937, 106.48585681286382, 116.84498979981964," +
      "90.4669896699453, 94.89824166120223, 99.32949365245918," +
      "94.36086016445655, 102.08160437494293, 107.66475640149092," +
      "90.66398332071275, 94.751753620845, 98.83952392097724, 5);",
    "INSERT INTO flight_comfort values(9, 3, 3, '2020-05-05 19:05:00'," +
      "76.2794997422115, 93.08860283035511, 106.85463598884454," +
      "91.94544923270679, 95.63126521092127, 99.43065325181989," +
      "79.63735481432722, 93.48203989651013, 104.99502725763219," +
      "92.11702024712994, 95.77383674947492, 99.43065325181989," +
      "81.03045668322513, 95.70985551569974, 106.66135317756554," +
      "92.39564062090952, 95.91314693636471, 99.43065325181989, 4);"};
  }