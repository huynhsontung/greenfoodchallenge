const functions = require('firebase-functions');

const admin = require("firebase-admin");

admin.initializeApp();

exports.getUserGender = functions.database.ref("/users/{")