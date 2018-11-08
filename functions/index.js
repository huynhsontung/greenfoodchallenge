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
                return (currentTotalAmountSum || 0) + finalAmount
            }
            else {
                return (currentTotalAmountSum || 0) - originalAmount + finalAmount
            }
        });
        return 0;
    });

exports.ModifyPledgeLocationWhenLocationChangedTriigger = functions.database.ref("/uids/{userUid}/pledgeInfo/location")
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

exports.ModifyPledgeLocationWhenAmountChangedTriigger = functions.database.ref("/uids/{userUid}/pledgeInfo/amount")
    .onWrite((change, context) => {

        var userUid = context.params.userUid;
        var finalAmount = change.after.val();

        if (finalAmount === 0) {
            
        }

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



