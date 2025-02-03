//登入登出按鈕
function navRegister() {
  window.location.href = "../pages/register.html";
}
function navLogin() {
  window.location.href = "../pages/login.html";
}
//menu
let isExpanded = false;

$("#btnMenu").on("click", function () {
  if (!isExpanded) {
    $("#menu").removeClass("hidden");
    $("#menu").animate({ left: "0" }, 300);
  } else {
    $("#menu").animate({ left: "-20rem" }, 300);
  }
  isExpanded = !isExpanded;
});
$("#closeMenu").on("click", function () {
  if (!isExpanded) {
    $("#menu").removeClass("hidden");
    $("#menu").animate({ left: "0" }, 300);
  } else {
    $("#menu").animate({ left: "-20rem" }, 300);
  }
  isExpanded = !isExpanded;
});

//搜尋
function search() {
  event.preventDefault();
  const query = $("#navbarInputSearch").prop("value");
  window.location.href = `../pages/search.html?query=${query}`;
}
//計算時間
function timeCal(calTime) {
  var date = new Date();
  var time = parseInt(date - Date.parse(calTime)) / 1000;
  var timeText = "";
  if (time < 60 * 60 * 24) {
    timeText = parseInt(time / 60 / 60) + "小時前";
    if (time < 60 * 60) {
      timeText = parseInt(time / 60) + "分鐘前";
      if (time < 60) {
        timeText = parseInt(time) + "秒前";
      }
    }
  } else {
    timeText = parseInt(time / 60 / 60 / 24) + "天前";
  }
  return timeText;
}
//通知
function notify() {
  $.ajax({
    url: "http://127.0.0.1:8080/notify/get",
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
      if (idx < 5) {
        if (item.checked == "false") {
          count += 1;
          $("#notifyUl").append(
            ` <a class="aNotify cursor-pointer"><li class="block px-8 py-4 my-2 hover:bg-gray-100 bg-blue-50 text-lg ">
        <p class="tracking-wider">${item.content}</p>
        <p class="text-sm text-gray-500">${timeCal(item.time)}</p>
        </li><input class="notifyId hidden" value=${
          item.notifyId
        } /><input class="courseId hidden" value=${item.courseId} /></a>`
          );
        } else {
          $("#notifyUl").append(
            ` <a class="aNotify cursor-pointer"> <li class="block px-8 py-4 my-2 hover:bg-gray-100 text-lg ">
        <p class="tracking-wider">${item.content}</p>
        <p class="text-sm text-gray-500">${timeCal(item.time)}</p>
        </li><input class="notifyId hidden" value=${
          item.notifyId
        } /><input class="courseId hidden" value=${item.courseId} /></a>`
          );
        }
      }
    });
    $("#notifyUl").append(
      `<a href="../pages/notify.html"
    ><p class="text-center py-2 hover:text-blue-500">查看所有通知</p></a
  >`
    );
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
  fetch("http://127.0.0.1:8080/user/logout", {
    method: "POST",
    credentials: "include",
  }).then(() => {
    window.location.href = "../index.html";
  });
}

//通知圖示點擊顯示div
$("#iconNotify").on("click", () => {
  if ($("#divNotify").hasClass("opacity-0")) {
    $("#divNotify").removeClass("opacity-0 scale-95 hidden");
    $("#divNotify").addClass("opacity-100 scale-100");
  } else {
    $("#divNotify").addClass("opacity-0 scale-95 hidden");
    $("#divNotify").removeClass("opacity-100 scale-100");
  }
});
$(document).on("click", ".aNotify", function () {
  let notifyVal = $(this).find(".notifyId").val();
  let courseVal = $(this).find(".courseId").val();
  async function api() {
    await $.ajax({
      url: `http://127.0.0.1:8080/notify/checkedNotify?notifyId=${notifyVal}`,
      method: "PUT",
      xhrFields: {
        withCredentials: true, // 設置為 true 以支持跨域請求時攜帶 cookie
      },
    }).done(() => {
      window.location.href = `../pages/lecture.html?course_id=${courseVal}`;
    });
  }
  api();
});
//讀取登入後導覽列資料
async function loadLogin() {
  const isLoginResponse = await fetch("http://127.0.0.1:8080/user/islogin", {
    method: "GET",
    credentials: "include",
  });
  const isLoggedIn = await isLoginResponse.text();

  if (isLoggedIn === "true") {
    navbarLog("Login");

    const loginDataResponse = await fetch(
      "http://127.0.0.1:8080/user/logindata",
      {
        method: "GET",
        credentials: "include",
      }
    );
    const loginData = await loginDataResponse.json();

    if (loginData.avatar != "noImg") {
      $(".navbarAvatar").prop("src", loginData.avatar);
    }
    $(".navbarUserName").text(loginData.userName);
    $(".navbarEmail").text(loginData.email);
    notify();
  } else {
    navbarLog("Logout");
  }
}
loadLogin();

$("#btnNavSearch").on("click", () => {
  if ($("#navbarFormSearch").hasClass("hidden")) {
    $("#navbarFormSearch").removeClass("hidden");
    $("#navbarFormSearch").addClass("flex");
  } else {
    $("#navbarFormSearch").addClass("hidden");
    $("#navbarFormSearch").removeClass("flex");
  }
});

//購物車
function loadNavbarCart() {
  $.ajax({
    url: "http://127.0.0.1:8080/api/cart",
    method: "GET",
    xhrFields: {
      withCredentials: true, // 設置為 true 以支持跨域請求時攜帶 cookie
    },
  }).done((data) => {
    let carts = data.data.items;
    $(".cartCount").remove();
    if (carts.length > 0 && carts.length < 10) {
      $("#cart").append(`
      <p class="rounded-full h-5 w-5 text-sm text-center tracking-tighter text-white bg-red-500 absolute -top-1 right-4 cartCount">
      ${carts.length}
      </p>`);
    } else if (carts.length >= 10) {
      $("#cart").append(`
        <p class="rounded-full h-5 w-5 text-sm text-center tracking-tighter text-white bg-red-500 absolute -top-1 right-4 cartCount">
        9+
        </p>`);
    }
  });
}
loadNavbarCart();
