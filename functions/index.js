const functions = require('firebase-functions');

const admin = require("firebase-admin");

admin.initializeApp();

exports.addTotalNumberOfPeoplePledged = functions.database.ref("/uids/{userUid}/pledgeInfo")
    .onWrite((change, context) => {
        var totalNumber = 0;
        var totalAmountSum = 0;
        var query1 = admin.database().ref("/uids");
        var query2 = admin.database().ref("/pledgeResult");
        return query1.once("value")
            .then((snapshot) => {
                snapshot.forEach((childSnapshot) => {
                    var amount = childSnapshot.child("pledgeInfo").child("amount").val();
                    // console.log("test", amount);
                    if (amount !== 0) {
                        totalNumber ++;
                        totalAmountSum += amount;
                    }
                    // console.log("test", totalNumber, totalAmountSum);
                });
                console.log("test", totalNumber, totalAmountSum);
                return query2.set({
                    totalNumber: totalNumber,
                    totalAmountSum: totalAmountSum
                });
                // query2.child("totalAmountSum").set(totalAmountSum);
            });
        // return change.after.ref.parent.child("pledge").once("value").then((snapshot) => {
        //     snapshot.forEach((childSnapshot) => {
        //        console.log("test", childSnapshot);
        //     });
        //     return 0
        // })
    });



