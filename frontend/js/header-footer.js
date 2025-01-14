async function loadNavbar() {
  const navbarResponse = await fetch("../components/navbar.html");
  const navbarHtml = await navbarResponse.text();
  $("#navbar").html(navbarHtml);

  const isLoginResponse = await fetch("http://localhost:8080/user/islogin", {
    method: "GET",
    credentials: "include",
  });
  const isLoggedIn = await isLoginResponse.text();

  if (isLoggedIn === "true") {
    navbarLog("Login");

    const loginDataResponse = await fetch(
      "http://localhost:8080/user/logindata",
      {
        method: "GET",
        credentials: "include",
      }
    );
    const loginData = await loginDataResponse.json();

    if (loginData.avatar !== "noImg") {
      $("#navbarAvatar1").prop("src", loginData.avatar);
      $("#navbarAvatar2").prop("src", loginData.avatar);
    }
    $("#navbarUserName").text(loginData.userName);
    $("#navbarEmail").text(loginData.email);
  } else {
    navbarLog("Logout");
  }
}

loadNavbar();

fetch("../components/footer.html")
  .then((response) => response.text())
  .then((data) => {
    $("#footer").html(data);
  });
