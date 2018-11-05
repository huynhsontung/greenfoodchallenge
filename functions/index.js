const functions = require('firebase-functions');

const admin = require("firebase-admin");

admin.initializeApp();

exports.getUserGender = functions.https.onCall((req, res) => {
   const userUid = req.userUid;

   return admin.database().ref('/users/' + userUid)
       .once('value')
       .then((snapshot) => {
           console.log("Check snapshot" + snapshot.child("gender").val());
           return snapshot.child("gender").val();
       });
});