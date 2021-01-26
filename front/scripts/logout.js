document.addEventListener("DOMContentLoaded", function () {
    if (sessionStorage.getItem('email') !== 'null' && sessionStorage.getItem('email')) {
            let loginBtn = document.getElementById("loginBtn");

            loginBtn.innerHTML = "Logout";
            loginBtn.setAttribute('href', 'index.html');
            loginBtn.setAttribute('onClick', 'logoutFunction()');

            let registerBtn = document.getElementById("registerBtn");
            registerBtn.style.display = "none";
            
            changeAdminBtn("block");
        }
});

function logoutFunction() {
    sessionStorage.setItem('email', 'null');
}

function changeAdminBtn(disp) {
    let adminBtn = document.getElementById("adminBtn");
    adminBtn.style.display = disp;
}