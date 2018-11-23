var XMLHttpRequest = require("xmlhttprequest").XMLHttpRequest;
const Http = new XMLHttpRequest();
const url = 'http://localhost:5000/mindfulmealplanner/us-central1/getRandomMealsbyLocation';

var data = JSON.stringify(
    {"location" : "Vancouver"});

Http.open("POST", url);
Http.setRequestHeader("Content-Type", "application/json")
Http.send(data);
// Http.onreadystatechange = (e) =>{
//     console.log(Http.responseText);
// };

// Http.open("GET", url);
// Http.send();
Http.onreadystatechange = () => {
    if (Http.readyState === 4) {
        console.log(Http.responseText);
    }
};


