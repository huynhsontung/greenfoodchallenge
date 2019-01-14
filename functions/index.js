const functions = require('firebase-functions');

const admin = require("firebase-admin");

admin.initializeApp();

var bucket = admin.storage().bucket();

const bucketName = "mindfulmealplanner.appspot.com";

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
            }).return;
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
            }).return;
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
            }).return;
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

    return admin.database().ref("/pledgeResult/pledgeLocation/" + location)
    .once("value").then((snapshot) => {
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

exports.getAllRestaurantMenu = functions.https.onCall((data) => {
    return admin.database().ref("/RestaurantMenu").once("value").then((snapshot) => {
        var allRestaurant = {};
        snapshot.forEach((childSnapshot) => {
            allRestaurant[childSnapshot.key] = childSnapshot.child("isGreen").val();
        });
        console.log("Check all restaurant: ", JSON.stringify(allRestaurant));
        return allRestaurant;
    });
});


exports.getRestaurantFoodMenu = functions.https.onCall((data) => {
    const restaurantName = data.restaurantName;
    console.log("Check : restaurantName", restaurantName);
    return admin.database().ref("/RestaurantMenu/" + restaurantName + "/foodList")
        .once("value").then((snapshot) => {
            var restaurantFoodMenu = {};
            snapshot.forEach((childSnapshot) => {
                restaurantFoodMenu[childSnapshot.key] = childSnapshot.val();
                console.log("check key and value", childSnapshot.key, childSnapshot.val());
            });
            console.log("Check all food menu: ", JSON.stringify(restaurantFoodMenu));
            return restaurantFoodMenu;
    });
});

exports.adminSetAllUsersPledgeAmountZero = functions.https.onRequest((req, resp) => {
    var logText = "";
    if(req.method !== "GET") {
        resp.status(400).send("Not GET request");
        console.log("Not GET request");
    }

    logText += "Start to set all users pledge amount to zero.\n";

    admin.database().ref("/uids").once("value").then((snapshot) => {
        snapshot.forEach((childSnapshot) => {

            var amountPath = childSnapshot.child('pledgeInfo').child('amount');

            logText += childSnapshot.key + ": " +
                amountPath.val() + "\n";

            if (amountPath.val() !== 0) {
                amountPath.ref.set(0);
            }
        });

        logText += "Finished.\n";
        console.log(logText);
        resp.status(200).send(logText);
        return null;
    }).return;
});

exports.adminScanAllUsersPublicMeal = functions.https.onRequest((req, resp) => {

    if(req.method !== "GET") {
        resp.status(400).send("Not GET request");
        console.log("Not GET request");
    }

    console.log("Start to scan.\n");

    admin.database().ref("/publicMeals").remove();

    admin.database().ref("/uids").once("value").then((snapshot) =>{

        var userUidList = [];
        var usersPublicMeal = {};
        snapshot.forEach((childSnapshot) => {

            if (childSnapshot.child("mealInfo").exists()) {

                var userUid = childSnapshot.key;
                userUidList.push(userUid);

                usersPublicMeal[userUid] = {
                    "userUid" : userUid,
                    "displayName" : childSnapshot.child("userInfo").child("displayName").val(),
                    "iconName" : childSnapshot.child("userInfo").child("iconName").val(),
                    "location" : childSnapshot.child("pledgeInfo").child("location").val()
                }
            }
        });
        // console.log(usersPublicMeal);
        return [userUidList, usersPublicMeal];
    }).then((data) => {

       const userUidList = data[0];
       const usersPublicMeal = data[1];

        userUidList.forEach((userUid) => {
            // console.log("start to scan user: ", userUid);
            admin.database().ref("/uids/" + userUid + "/mealInfo").once("value").then((snapshot) => {
               snapshot.forEach((childSnapshot) => {
                    if (!childSnapshot.child("isPrivate").val()) {
                       var mealData = usersPublicMeal[userUid];
                       mealData["mealName"] = childSnapshot.key;
                       mealData["totalCo2eAmount"] = childSnapshot.child("totalCo2eAmount").val();
                       mealData["isGreen"] = childSnapshot.child("isGreen").val();
                       mealData["tags"] = childSnapshot.child("tags").val();
                       mealData["restaurantName"] = childSnapshot.child("restaurantName").val();
                       mealData["foodList"] = childSnapshot.child("foodList").val();
                       console.log(mealData);
                       admin.database().ref("/publicMeals").push().set(mealData);
                    }
                });

                return null;
            }).return;
        });
        return null;
    }).return;

    resp.status(200).send("Ok.");
});

exports.getRandomMeals = functions.https.onCall((data) =>{

    var number = data.number;

    return admin.database().ref("/publicMeals").once("value").then((snapshot) => {

        var mealKeyList = [];
        snapshot.forEach((childSnapshot) => {
           mealKeyList.push(childSnapshot.key)
        });

        var resultList = getRandomItemsInList(mealKeyList, 5);
        console.log(resultList);

        var mealData = {};
        resultList.forEach((key) => {
           mealData[key] = snapshot.child(key).val();
        });

        console.log(mealData);

        return mealData;
    });
});


exports.getRandomMealsbyLocation = functions.https.onCall((data) => {
    const number = data.location;
    const location = data.location;

    console.log(location);

    return admin.database().ref("/publicMeals").once("value").then((snapshot) => {
        var mealKeyList = [];
        snapshot.forEach((childSnapshot) => {
           if (childSnapshot.child("location").val() === location) {
               mealKeyList.push(childSnapshot.key);
           }
        });
       // console.log(mealKeyList);
        var resultList = getRandomItemsInList(mealKeyList, 5);
        console.log(resultList);

        var mealData = {};
        resultList.forEach((key) => {
           mealData[key] = snapshot.child(key).val();
        });

        console.log(mealData);
        return mealData;
    });

});

function getRandomItemsInList(list, num) {
    var tempSet = new Set();

    while (tempSet.size !== num) {
        var item = list[Math.floor(Math.random()*list.length)];
        tempSet.add(item);
    }

    return Array.from(tempSet);
}

exports.CollectPublicMealsTrigger = functions.database.ref("/uids/{userUid}/mealInfo/{mealName}")
    .onWrite((change, context) => {

    var userUid = context.params.userUid;
    var mealName = context.params.mealName;
    var mealObj = change.after.val();

    console.log("original meal: ",mealObj);

    // copy meal from user's meal list to publicMeals directory
    var publicMealsRef = admin.database().ref("/publicMeals/" + mealName);
    if (mealObj !== null){
        if(mealObj.isPrivate === false ){
            admin.database().ref("/uids/" + userUid + "/userInfo/")
            .once("value").then((snapshot) => {
                // introduce new fields when copying, these fields include:
                // user's display name; icon
                let data = snapshot.val();
                mealObj.author = {
                    displayName: data.displayName,
                    iconName: data.iconName,
                    userUid: userUid
                }
                console.log("meal with user info: ", mealObj);
                return mealObj;
            }).then((mealObj) => {
                publicMealsRef.transaction((snapshot) => {
                    // if meal not already exist in publicMeals, add metrics info to meal
                    if (snapshot === null){
                        mealObj.metrics = {views: 0, likes: 0, shares:0};
                    } else {
                        mealObj.metrics = snapshot.metrics;
                    }
                    return mealObj;
                });
                return;
            }).return;
        } else {
            mealObj = null;
            publicMealsRef.transaction(() => {return mealObj;});
        }
    } else {
        // mealObj === null -> it has been deleted -> need to delete images from Storage
        let mealObjBefore = change.before;
        mealObjBefore.child("foodList").forEach((foodItem)=>{
            let storageRef = foodItem.child("storageReference").val();
            console.log("image to delete: ", storageRef);
            try{
                // storageRef has form "gs://mindfulmealplanner.appspot.com/[FILE_PATH]"
                // to extract FILE PATH ...
                let toBeRemove = "gs://"+bucketName+"/";
                bucket.file(storageRef.substring(toBeRemove.length)).delete();
            } catch(e) {
                console.log(e);
            }
        });
        publicMealsRef.transaction(() => {return mealObj;});
    }
    return 0;
});

exports.getFilteredMealList = functions.https.onCall((data,context) => {
    const filter = data.filter;
    var end = data.last;
    var range = data.range;
    const uid = context.auth.uid;
    console.log(filter, "; ", range, "; ", end, "; ", uid);

    if (range === null){
        range = 20;
    }
    var query;
    switch(filter){
        case "self": 
            return admin.database().ref("/uids/"+uid+"/mealInfo/")
                .orderByKey()
                .once("value").then((snapshot)=> {return snapshot.val();});
        case "recent": 
            if (end !==null) {
                query = admin.database().ref("/publicMeals/")
                .orderByKey().endAt(end).limitToLast(range);
            } else {
                query = admin.database().ref("/publicMeals/")
                .orderByKey().limitToLast(range);
            }
            return query.once("value").then((snapshot) => {return snapshot.val();});
        default:
            if (end !==null) {
                query = admin.database().ref("/publicMeals/")
                .orderByChild("metrics/likes").endAt(end).limitToLast(range)
            } else {
                query = admin.database().ref("/publicMeals/")
                .orderByChild("metrics/likes").limitToLast(range)
            }
            return query.once("value").then((snapshot) => {
                    console.log(snapshot.val());
                    return snapshot.val();
                });
    }
});