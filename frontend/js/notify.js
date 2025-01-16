//顯示div
$(document).on("click", "#btnMenu", function () {
  if ($("#divMenu").hasClass("hidden")) {
    $(".divMenu").addClass("hidden");
    $("#divMenu").removeClass("hidden");
  } else {
    $("#divMenu").addClass("hidden");
  }
});
$(document).on("click", ".btnMenu", function () {
  const $menu = $(this).siblings("div").first();
  if ($menu.hasClass("hidden")) {
    $(".divMenu").addClass("hidden");
    $("#divMenu").addClass("hidden");
    $menu.removeClass("hidden");
  } else {
    $menu.addClass("hidden");
  }
});
//計算時間
function timeCal(calTime) {
  let date = new Date();
  let time = parseInt(date - Date.parse(calTime)) / 1000;
  let timeText = "";
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
//顯示通知結果
async function res() {
  await $.ajax({
    url: "http://localhost:8080/notify/get",
    method: "GET",
    xhrFields: {
      withCredentials: true, // 設置為 true 以支持跨域請求時攜帶 cookie
    },
  }).done((data) => {
    $("#divResults").empty();
    $("#divCount").html(`<p
                class="text-3xl flex justify-between pl-8 pr-4 pb-4 mb-4 min-h-56"
              >
                無通知
              </p>`);
    if (data.length != 0) {
      $("#divCount").html(
        `              <div
                    class="text-3xl flex justify-between pl-8 pr-4 pb-4 mb-4 border-b-4 border-black relative"
                  >
                    <p>${data.length}則通知</p>
                  <button
                    id="btnMenu"
                    class="text-gray-500 hover:text-black rounded-md duration-300 text-4xl"
                  >
                    <i class="bi bi-list"></i>
                  </button>
                  <div
                    id="divMenu"
                    class="z-[1] hidden absolute overflow-y-auto overflow-x-hidden right-0 top-10 w-64 bg-white border border-gray-200 shadow-lg"
                  >
                    <ul class="py-1 px-3 text-xl">
                      <li class="my-2 p-2 cursor-pointer hover:bg-gray-100" id="checkedAll">
                        <i class="bi bi-check-lg"></i>　全部標示為已讀
                      </li>
                      <li class="my-2 p-2 cursor-pointer hover:bg-gray-100" id="deleteAll">
                        <i class="bi bi-trash"></i>　刪除全部通知
                      </li>
                    </ul>
                  </div>
                  </div>`
      );
    }
    $.each(data, (idx, item) => {
      if (item.checked == "false") {
        $("#divResults").append(`
        <div
                class="relative bg-blue-50 hover:bg-gray-100 shadow-md border-2 text-xl mx-4 my-2 rounded-lg tracking-wide leading-relaxed"
              >
                <a class="aNotify cursor-pointer">
                  <div class="min-h-36 py-4 pl-8 pr-12 flex flex-col justify-between">
                  <p>${item.content}</p>
                  <div class="flex justify-start">
                  <p class="text-lg text-gray-500">${timeCal(item.time)}</p>
                  <p class="text-lg text-gray-600 ml-6">${
                    item.courseName
                  }</p></div>
                  </div>
                </a>
                <button
                  class="btnMenu absolute top-2 right-2 text-3xl text-gray-500 hover:text-black duration-300"
                >
                  <i class="bi bi-list"></i>
                </button>
                <div
                  class="z-[1] divMenu  hidden absolute overflow-y-auto overflow-x-hidden -right-3 top-10 w-72 bg-white border border-gray-200 shadow-lg"
                >
                  <ul class="py-1 px-3 text-lg">
                    <li class="my-1 p-2 cursor-pointer hover:bg-gray-100 checkedNotify">
                      <i class="bi bi-check-lg"></i>　標示為已讀
                    </li>
                    <li class="my-1 p-2 cursor-pointer hover:bg-gray-100 checkedCourse">
                      <i class="bi bi-list-check"></i>　相同課程標示為已讀
                    </li>
                    <li class="my-1 p-2 cursor-pointer hover:bg-gray-100 deleteNotify">
                      <i class="bi bi-trash"></i>　刪除該通知
                    </li>
                    <li class="my-1 p-2 cursor-pointer hover:bg-gray-100 deleteCourse">
                      <i class="bi bi-trash-fill"></i>　刪除相同課程通知
                    </li>
                    <input class="hidden courseId" value=${item.courseId} />
                    <input class="hidden notifyId" value=${item.notifyId} />
                  </ul>
                </div>
              </div>
              `);
      } else {
        $("#divResults").append(`
            <div
                    class="relative hover:bg-gray-100 shadow-md border-2 text-xl mx-4 my-2 rounded-lg tracking-wide leading-relaxed"
                  >
                  <a class="aNotify cursor-pointer">
                      <div class="min-h-36 py-4 pl-8 pr-12 flex flex-col justify-between">
                      <p>${item.content}</p>
                <div class="flex justify-start">
                  <p class="text-lg text-gray-500">${timeCal(item.time)}</p>
                  <p class="text-lg text-gray-600 ml-6">${
                    item.courseName
                  }</p></div>
                  </div>
                   </a>
                    <button
                      class="btnMenu absolute top-2 right-2 text-3xl text-gray-500 hover:text-black duration-300"
                    >
                      <i class="bi bi-list"></i>
                    </button>
                    <div
                      class="z-[1] divMenu hidden absolute overflow-y-auto overflow-x-hidden -right-3 top-10 w-72 bg-white border border-gray-200 shadow-lg"
                    >
                      <ul class="py-1 px-3 text-lg">
                        <li class="my-1 p-2 cursor-pointer hover:bg-gray-100 checkedNotify">
                          <i class="bi bi-check-lg"></i>　標示為已讀
                        </li>
                        <li class="my-1 p-2 cursor-pointer hover:bg-gray-100 checkedCourse">
                          <i class="bi bi-list-check"></i>　相同課程標示為已讀
                        </li>
                        <li class="my-1 p-2 cursor-pointer hover:bg-gray-100 deleteNotify">
                          <i class="bi bi-trash"></i>　刪除該通知
                        </li>
                        <li class="my-1 p-2 cursor-pointer hover:bg-gray-100 deleteCourse">
                          <i class="bi bi-trash-fill"></i>　刪除相同課程通知
                        </li>
                        <input class="hidden courseId" value=${item.courseId} />
                        <input class="hidden notifyId" value=${item.notifyId} />
                      </ul>
                    </div>
                  </div>
                  `);
      }
    });
  });
}
async function islogin() {
  const isLoginResponse = await fetch("http://localhost:8080/user/islogin", {
    method: "GET",
    credentials: "include",
  });
  const isLoggedIn = await isLoginResponse.text();
  if (isLoggedIn != "true") {
    window.location.href = "../index.html";
  } else {
    res();
  }
}
islogin();

$(document).on("click", ".aNotify", function () {
  let courseVal = $(this).siblings(".divMenu").find(".courseId").val();
  let notifyVal = $(this).siblings(".divMenu").find(".notifyId").val();
  async function api() {
    await $.ajax({
      url: `http://localhost:8080/notify/checkedNotify?notifyId=${notifyVal}`,
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

//按鈕api
$(document).on("click", "#checkedAll", function () {
  async function api() {
    await $.ajax({
      url: "http://localhost:8080/notify/checkedAll",
      method: "PUT",
      xhrFields: {
        withCredentials: true, // 設置為 true 以支持跨域請求時攜帶 cookie
      },
    }).done(() => {
      res();
    });
  }
  api();
});
$(document).on("click", "#deleteAll", function () {
  async function api() {
    await $.ajax({
      url: "http://localhost:8080/notify/deleteAll",
      method: "DELETE",
      xhrFields: {
        withCredentials: true, // 設置為 true 以支持跨域請求時攜帶 cookie
      },
    }).done(() => {
      res();
    });
  }
  api();
});
$(document).on("click", ".checkedNotify", function () {
  let val = $(this).siblings(".notifyId").first().val();
  async function api() {
    await $.ajax({
      url: `http://localhost:8080/notify/checkedNotify?notifyId=${val}`,
      method: "PUT",
      xhrFields: {
        withCredentials: true, // 設置為 true 以支持跨域請求時攜帶 cookie
      },
    }).done(() => {
      res();
    });
  }
  api();
});
$(document).on("click", ".checkedCourse", function () {
  let val = $(this).siblings(".courseId").first().val();
  async function api() {
    await $.ajax({
      url: `http://localhost:8080/notify/checkedCourse?courseId=${val}`,
      method: "PUT",
      xhrFields: {
        withCredentials: true, // 設置為 true 以支持跨域請求時攜帶 cookie
      },
    }).done(() => {
      res();
    });
  }
  api();
});
$(document).on("click", ".deleteNotify", function () {
  let val = $(this).siblings(".notifyId").first().val();
  async function api() {
    await $.ajax({
      url: `http://localhost:8080/notify/deleteNotify?notifyId=${val}`,
      method: "DELETE",
      xhrFields: {
        withCredentials: true, // 設置為 true 以支持跨域請求時攜帶 cookie
      },
    }).done(() => {
      res();
    });
  }
  api();
});
$(document).on("click", ".deleteCourse", function () {
  let val = $(this).siblings(".courseId").first().val();
  async function api() {
    await $.ajax({
      url: `http://localhost:8080/notify/deleteCourse?courseId=${val}`,
      method: "DELETE",
      xhrFields: {
        withCredentials: true, // 設置為 true 以支持跨域請求時攜帶 cookie
      },
    }).done(() => {
      res();
    });
  }
  api();
});
