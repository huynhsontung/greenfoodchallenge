const functions = require('firebase-functions');

const admin = require("firebase-admin");

admin.initializeApp();

exports.CountTotalNumberOfUsersPledgedTrigger = functions.database.ref("/uids/{userUid}/pledgeInfo/amount")
    .onWrite((change, context) => {

        var originalAmount = change.before.val();
        var finalAmount = change.after.val();

        console.log("Check original and final data: ", originalAmount, finalAmount);

        var totalNumber = admin.database().ref("/pledgeResult/totalNumber");

        totalNumber.transaction((currentTotalNumber) => {
           return (currentTotalNumber || 0) + isUserPledge(originalAmount, finalAmount);
        });
        return 0;
    });

function isUserPledge(originalAmount, finalAmount) {
    if (originalAmount === null && finalAmount === 0) {
        console.log("isUserPledge: 1");
        return 0;
    }

    if (originalAmount ===null && finalAmount !== 0) {
        console.log("isUserPledge: 2");
        return 1
    }

    if (originalAmount === 0 && finalAmount !== 0) {
        console.log("isUserPledge: 3");
        return 1
    }
    if (originalAmount !== 0 && finalAmount !== 0) {
        console.log("isUserPledge: 4");
        return 0;
    }
    if (originalAmount !== 0 && finalAmount === 0) {
        console.log("isUserPledge: 5");
        return -1;
    }
    console.log("isUserPledge Error");
    return null;
}


exports.CountTotalAmountOfUsersPledgedTrigger = functions.database.ref("/uids/{userUid}/pledgeInfo/amount")
    .onWrite((change, context) => {

        var originalAmount = change.before.val();
        var finalAmount = change.after.val();

        console.log("Check original and final data: ", originalAmount, finalAmount);

        var totalAmountSum = admin.database().ref("/pledgeResult/totalAmountSum");

        totalAmountSum.transaction((currentTotalAmountSum) => {
            if (originalAmount === null) {
                return (currentTotalAmountSum || 0) + finalAmount;
            }
            else {
                return (currentTotalAmountSum || 0) - originalAmount + finalAmount;
            }
        });
        return 0;
    });

exports.ModifyPledgeLocationWhenLocationChangedTrigger = functions.database.ref("/uids/{userUid}/pledgeInfo/location")
    .onWrite((change, context) => {

        var userUid = context.params.userUid;
        var originalLocation = change.before.val();
        var finalLocation = change.after.val();

        console.log("Check userUid, original and final data: ", userUid, originalLocation, finalLocation);


        admin.database().ref("/uids/" + userUid + "/pledgeInfo/amount")
            .once("value").then((snapshot) => {
                userPlesgeAmount = snapshot.val();
                console.log("Check user pledge amount: ", userPlesgeAmount);
                if (userPlesgeAmount !== 0) {
                    // add new location
                    addUserToLocationList(finalLocation, userUid);
                    // delete old location
                    removeUserFromLocationList(originalLocation, userUid);
                }
                return 0;
            });
        return 0;
    });

exports.ModifyPledgeLocationWhenAmountChangedTrigger = functions.database.ref("/uids/{userUid}/pledgeInfo/amount")
    .onWrite((change, context) => {

        var userUid = context.params.userUid;

        var originalAmount = change.before.val();
        var finalAmount = change.after.val();

        admin.database().ref("/uids/" + userUid + "/pledgeInfo/location")
            .once("value").then((snapshot) =>{
                userLocation = snapshot.val();
                console.log("Check user final amount and location", finalAmount, userLocation);

                if (originalAmount !== 0 && finalAmount !== 0) {
                    return 0;
                }

                if (finalAmount === 0 ) {
                    removeUserFromLocationList(userLocation, userUid);
                }
                else {
                    addUserToLocationList(userLocation, userUid);
                }
                return 0;
            });
        return 0;
    });


function addUserToLocationList(location, userUid) {
    var pledgeLocation = admin.database().ref("/pledgeResult/pledgeLocation");
    pledgeLocation.child(location).push().set(userUid);
}

function removeUserFromLocationList(location, userUid) {
    if (location !== null) {
        admin.database().ref("/pledgeResult/pledgeLocation")
            .child(location).once("value").then((snapshot) => {
                snapshot.forEach((childSnapshot) => {
                    console.log("Check key and value: ", childSnapshot.key, childSnapshot.val());
                    if (childSnapshot.val() === userUid) {
                        childSnapshot.ref.remove();
                    }
                });
                return 0;
            });
    }
}

exports.UserDataDeletionTrigger = functions.database.ref("/uids/{userUid}")
    .onDelete((snapshot, context) => {

        var userUid = context.params.userUid;

        var userPledgeAmount = snapshot.child("pledgeInfo").child("amount").val();
        var userPledgeLocation = snapshot.child("pledgeInfo").child("location").val();

        console.log("Check user pledge amount and location", userPledgeAmount, userPledgeLocation);

        var totalAmountSum = admin.database().ref("/pledgeResult/totalAmountSum");
        totalAmountSum.transaction((currentTotalAmountSum) =>{
           return (currentTotalAmountSum || 0) - userPledgeAmount;
        });

        var totalNumber = admin.database().ref("/pledgeResult/totalNumber");
        totalNumber.transaction((currentTotalNumber) => {
            return (currentTotalNumber || 0) - 1;
        });

        removeUserFromLocationList(userPledgeLocation, userUid);

        return 0;
    });

exports.getUsersDataByLocation = functions.https.onCall((data) => {

    const location = data.location;

    return admin.database().ref("/pledgeResult/pledgeLocation/" + location).once("value").then((snapshot) => {
        var userUidList = [];
        snapshot.forEach((childSnapshot) => {
            userUidList.push(childSnapshot.val());
        });
        return userUidList;
    }).then((userUidList) => {
       console.log("Check userUid list", userUidList);
       return admin.database().ref("/uids").once("value").then((snapshot) => {
           var usersData = {};
           snapshot.forEach((childSnapshot) => {
               var usrUid = childSnapshot.key;
               if (userUidList.includes(usrUid)) {
                   console.log("test", usrUid, childSnapshot.val());
                   usersData[usrUid] = {
                       displayName : childSnapshot.child('userInfo').child('displayName').val(),
                       iconName: childSnapshot.child('userInfo').child('iconName').val(),
                       amount : childSnapshot.child('pledgeInfo').child('amount').val()
                   }
               }
           });
           console.log("Check users data:", usersData);
           return usersData;
       });
    });
});

// exports.fakeUserCreateTrigger = functions.auth.user().onCreate(user => {
//     var genderList = ['male', 'female'];
//     var locationList = ['Vancouver', 'Burnaby', 'West Vancouver', 'North Vancouver', 'Richmond', 'Coquitlam', 'Surrey', 'Langley'];
//     var iconNameList = ['android', 'chicken', 'egg', 'fish', 'meat', 'moon', 'star', 'sun', 'tree'];
//
//
//     var gender = genderList[Math.floor(Math.random()*genderList.length)];
//     var location = locationList[Math.floor(Math.random()*locationList.length)];
//     var iconName = iconNameList[Math.floor(Math.random()*iconNameList.length)];
//
//     var random = require('./random-name');
//     var displayname = random.first() + ' ' + random.last();
//
//     var amount = Math.ceil(Math.random() * 200);
//
//
//     var uid = user.uid;
//     var email = user.email;
//     var userData =
//         {
//             "planInfo" : {
//                 "Plan1": {
//                     "beans": 74,
//                     "beef": 105,
//                     "chicken": 102,
//                     "eggs": 41,
//                     "fish": 120,
//                     "pork": 40,
//                     "vegetables": 185
//                 }
//             },
//
//             "pledgeInfo" : {
//                 "amount" : amount,
//                 "location" : location
//             },
//
//             "userInfo" : {
//                 "currentPlanName" : "Plan1",
//                 "displayName" : displayname,
//                 "email" : email,
//                 "gender" : gender,
//                 "iconName" : iconName
//             }
//         };
//
//     console.log("Check user data", userData);
//     admin.database().ref('/uids/' + uid).set(userData);
//     return 0;
// });

//
// exports.test = functions.https.onRequest((req, resp) => {
//     if(req.method !== "POST") {
//         resp.status(400).send("not POST request");
//         console.log("test", "not POST request");
//     }
//
//     console.log("check type" , typeof req.body);
//     var data = req.body;
//
//     if (typeof data === "string") {
//         console.log("string")
//         data = JSON.parse(data);
//     }
//     else if (typeof data === "object") {
//         console.log("object")
//     }
//     //
//     console.log("test req body", data);
//     console.log("test name", data.user.name);
//     console.log("test age", data.user.age);
//
//     resp.status(200).send("OK");
//
// });

// exports.adminCheckAndModifyTotalPledgeNumber = functions.https.onRequest((req, resp) => {
//     var log = "";
//     if(req.method !== "GET") {
//         resp.status(400).send("Not GET request");
//         console.log("Not GET request");
//     }
//
//     var oldAmount =
//
//
// });
