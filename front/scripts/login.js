document.querySelector("#login-form").addEventListener("submit", function (e) {
    e.preventDefault();
    var urlBase = "http://ec2-18-196-187-178.eu-central-1.compute.amazonaws.com:8082";
    

    fetch(urlBase + "/login", {
            method: "POST",
            body: JSON.stringify({
                email: document.getElementById('login-email').value,
                password: document.getElementById('login-password').value
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
                sessionStorage.setItem('email', myJson.email);
                window.location.href = "index.html";
            }
        })
        .catch(err => {
            alert(err);
        });
});