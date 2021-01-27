document.querySelector("#login-form").addEventListener("submit", function (e) {
    e.preventDefault();
    var baseUrl = "http://ec2-3-122-225-224.eu-central-1.compute.amazonaws.com:8082";

    fetch(baseUrl + "/register", {
            method: "POST",
            body: JSON.stringify({
                firstName: document.getElementById('signup-first-name').value,
                lastName: document.getElementById('signup-last-name').value,
                email: document.getElementById('signup-email').value,
                password: document.getElementById('signup-confirm-password').value,
            }),
            headers: {
                'Content-Type': 'application/json'
            }
        })
        .then(function (response) {
            return response.json();
        })
        .then(function (myJson) {
            if (myJson.message != null) {
                document.getElementById("error").innerText = myJson.message;
            } else {
                window.location.href = "login.html";
            }
        })
        .catch(err => {
            console.log(err);
        });
});