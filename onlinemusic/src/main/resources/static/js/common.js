$(document).ajaxError(function (e, xhr, opt, exc) {
    if (xhr != null && xhr.status == 401) {
        console.log(xhr);
        location.href = "login.html";
    }
    location.href="error.html";
});


function logout() {
    localStorage.removeItem("user_token");
    location.href = "blog_login.html";
}