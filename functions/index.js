const functions = require('firebase-functions');

const admin = require("firebase-admin");

admin.initializeApp();

exports.totalNumberOfUsersPledgedTrigger = functions.database.ref("/uids/{userUid}/pledgeInfo/amount")
    .onWrite((change, context) => {

        var originalAmount = change.before.val();
        var finalAmount = change.after.val();

        console.log("test", originalAmount, finalAmount);

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


exports.totalAmountOfUsersPledgedTrigger = functions.database.ref("/uids/{userUid}/pledgeInfo/amount")
    .onWrite((change, context) => {

        var originalAmount = change.before.val();
        var finalAmount = change.after.val();

        console.log("test", originalAmount, finalAmount);

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

exports.localOfUsersPledgedTriigger = functions.database.ref("/uids/{userUid}/pledgeInfo/location")
    .onWrite((change, context) => {

        var userUid = context.params.userUid;
        var originalLocation = change.before.val();
        var finalLocation = change.after.val();

        console.log("test", userUid, originalLocation, finalLocation);

        var pledgeLocation = admin.database().ref("/pledgeResult/pledgeLocation");
        pledgeLocation.child(finalLocation).push().set(userUid);
        return 0;
    });



