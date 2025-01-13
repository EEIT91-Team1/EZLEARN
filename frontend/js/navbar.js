$("#iconNotify").on("click", () => {
  console.log(123);
  if ($("#divNotify").hasClass("opacity-0")) {
    $("#divNotify").removeClass(
      "opacity-0 scale-95 hidden"
    );
    $("#divNotify").addClass("opacity-100 scale-100");
  } else {
    $("#divNotify").addClass("opacity-0 scale-95 hidden");
    $("#divNotify").removeClass("opacity-100 scale-100");
  }
  $("#notifyCount").remove();
});

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
      if (idx < 5) {
        if (item.checked == "false") {
          count += 1;
          $("#notifyUl").append(
            ` <a href="../pages/lecture.html?course_id=${
              item.courseId
            }"><li class="block px-8 py-4 my-2 hover:bg-gray-100 bg-blue-50 text-lg ">
          <p class="tracking-wider">${item.content}</p>
          <p class="text-sm text-gray-500">${timeCal(
            item.time
          )}</p>
          </li></a>`
          );
        } else {
          $("#notifyUl").append(
            `<a href="../pages/lecture.html?courseId=${
              item.courseId
            }"> <li class="block px-8 py-4 my-2 hover:bg-gray-100 text-lg ">
          <p class="tracking-wider">${item.content}</p>
          <p class="text-sm text-gray-500">${timeCal(
            item.time
          )}</p>
          </li></a>`
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
