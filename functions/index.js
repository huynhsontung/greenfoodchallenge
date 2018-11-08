const functions = require('firebase-functions');

const admin = require("firebase-admin");

admin.initializeApp();

exports.totalNumberOfUsersPledgedTrigger = functions.database.ref("/uids/{userUid}/pledgeInfo/amount")
    .onWrite((change, context) => {

        var originalAmount = change.before.val();
        var finalAmount = change.after.val();

        console.log("test", originalAmount, finalAmount);

        var totalNumber = admin.database().ref("/pledgeResult/totalNumber");

        return totalNumber.once("value").then((snapshot) => {
            var totalNumberVal;
            if (!snapshot.exists()) {
                totalNumberVal = isUserPledge(originalAmount, finalAmount);
            }
            else {
                totalNumberVal = snapshot.val();
                totalNumberVal = totalNumberVal + isUserPledge(originalAmount, finalAmount);
            }

            return totalNumber.set(totalNumberVal);
        });
    });

function isUserPledge(originalAmount, finalAmount) {
    if (originalAmount === 0 && finalAmount !== 0) {
        return 1
    }
    if (originalAmount !== 0 && finalAmount !== 0) {
        return 0;
    }
    if (originalAmount !== 0 && finalAmount === 0) {
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

        return totalAmountSum.once("value").then((snapshot) => {
            var totalNumberVal;
            if (!snapshot.exists()) {
                totalAmountSumValue = finalAmount;
            }
            else {
                totalAmountSumValue = snapshot.val();
                totalAmountSumValue = totalAmountSumValue - originalAmount + finalAmount;
            }
            return totalAmountSum.set(totalAmountSumValue);
        });
    });

exports.localOfUsersPledgedTriigger = functions.database.ref("/uids/{userUid}/pledgeInfo/location")
    .onWrite((change, context) => {
        // var change.before
        // var bar = context.params.userUid;
        // var data = change.after.ref;
    });



