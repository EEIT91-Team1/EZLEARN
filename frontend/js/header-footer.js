//登入登出按鈕
function navRegister() {
  window.location.href = "../pages/register.html";
}
function navLogin() {
  window.location.href = "../pages/login.html";
}
//搜尋
function search() {
  event.preventDefault();
  const query = $("#navbarInputSearch").prop("value");
  window.location.href = `../pages/search.html?query=${query}`;
}
//確認是否登入顯示div
function navbarLogin() {
  $("#navbarDivLogin").removeClass("hidden");
  $("#navbarDivLogout").addClass("hidden");
}

function logout() {
  fetch("http://localhost:8080/user/logout", {
    method: "POST",
    credentials: "include",
  }).then(() => {
    window.location.href = "../index.html";
  });
}

fetch("http://localhost:8080/user/islogin", {
  method: "get",
  credentials: "include",
})
  .then((response) => response.text())
  .then((data) => {
    console.log(123);
    if (data == "true") {
      navbarLogin();
      console.log(456);
      fetch("http://localhost:8080/user/logindata", {
        method: "get",
        credentials: "include",
      })
        .then((response) => response.json())
        .then((data) => {
          console.log(data);
          if (data.avatar != "noImg") {
            $("#navbarAvatar1").prop("src", data.avatar);
            $("#navbarAvatar2").prop("src", data.avatar);
          }
          $("#navbarUserName").text(data.userName);
          $("#navbarEmail").text(data.email);
        });
    }
  });

fetch("../components/navbar.html")
  .then((response) => response.text())
  .then((data) => {
    $("#navbar").html(data);
  });

fetch("../components/footer.html")
  .then((response) => response.text())
  .then((data) => {
    $("#footer").html(data);
  });
