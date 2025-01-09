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
//通知

function notify() {
  $.ajax({
    url: "http://localhost:8080/notify/get",
    method: "GET",
    xhrFields: {
      withCredentials: true, // 設置為 true 以支持跨域請求時攜帶 cookie
    },
  }).done((data) => {
    if (data.length == 0) {
      $("#notifyUl").append(
        ` <li>
      <p
      class="block px-8 py-4 my-2 text-lg tracking-wider h-24"
      >無通知</
      >
      </li>`
      );
    }
    let count = 0;
    $.each(data, function (idx, item) {
      if (item.checked == "false") {
        count += 1;
        $("#notifyUl").append(
          ` <li>
        <p
        class="block px-8 py-4 my-2 hover:bg-gray-100 bg-cyan-50 text-lg tracking-wider h-24"
        >${item.content}</
        >
        </li>`
        );
      } else {
        $("#notifyUl").append(
          ` <li>
        <p
        class="block px-8 py-4 my-2 hover:bg-gray-100 text-lg tracking-wider h-24"
        >${item.content}</
        >
        </li>`
        );
      }
    });
    if (count > 0 && count < 10) {
      $("#notify").append(`
      <p id="notifyCount" class="rounded-full h-5 w-5 text-sm text-center tracking-tighter text-white bg-red-500 absolute -top-1 right-4">
      ${count}
      </p>`);
    } else if (count >= 10) {
      $("#notify").append(`
        <p id="notifyCount" class="rounded-full h-5 w-5 text-sm text-center tracking-tighter text-white bg-red-500 absolute -top-1 right-4">
        9+
        </p>`);
    }
  });
}

//確認是否登入顯示div
function navbarLog(log) {
  $(`#navbarDiv${log}`).removeClass("hidden");
  $(`#navbarDiv${log}`).addClass("flex");
}

function logout() {
  fetch("http://localhost:8080/user/logout", {
    method: "POST",
    credentials: "include",
  }).then(() => {
    window.location.href = "../index.html";
  });
}

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
    notify();
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
