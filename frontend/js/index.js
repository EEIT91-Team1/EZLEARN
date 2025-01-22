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
//防止手機橫屏
$("#phone").on("click", () => {
  $("#phone").addClass("hidden");
});

$(document).ready(function () {
  //課程推薦輪播
  const recommedCarousel = document.getElementById("recommedCarousel");
  const btnRecommedPrev = document.getElementById("btnRecommedPrev");
  const btnRecommedNext = document.getElementById("btnRecommedNext");
  const recommedPage = document.getElementById("recommedPage");
  let currentRecommedPage = 1;
  let currentRecommedIndex = 0;

  const updateRecommedCarousel = () => {
    const offset = -currentRecommedIndex * 100;
    recommedCarousel.style.transform = `translateX(${offset}%)`;
  };

  btnRecommedPrev.addEventListener("click", () => {
    currentRecommedIndex =
      currentRecommedIndex > 0 ? currentRecommedIndex - 1 : 2;
    updateRecommedCarousel();
    currentRecommedPage == 1
      ? (currentRecommedPage = 3)
      : (currentRecommedPage = currentRecommedPage - 1);
    recommedPage.innerText = currentRecommedPage;
  });

  btnRecommedNext.addEventListener("click", () => {
    currentRecommedIndex =
      currentRecommedIndex < 2 ? currentRecommedIndex + 1 : 0;
    updateRecommedCarousel();
    currentRecommedPage == 3
      ? (currentRecommedPage = 1)
      : (currentRecommedPage = currentRecommedPage + 1);

    recommedPage.innerText = currentRecommedPage;
  });
  //---------------------------------------------------------------
  //學員評論輪播
  const reviewCarousel = document.getElementById("reviewCarousel");
  const btnReviewPrev = document.getElementById("btnReviewPrev");
  const btnReviewNext = document.getElementById("btnReviewNext");
  const reviewPage = document.getElementById("reviewPage");
  let currentReviewPage = 1;
  let currentReviewIndex = 0;

  const updateReviewCarousel = () => {
    const offset = -currentReviewIndex * 100;
    reviewCarousel.style.transform = `translateX(${offset}%)`;
  };

  btnReviewPrev.addEventListener("click", () => {
    currentReviewIndex = currentReviewIndex > 0 ? currentReviewIndex - 1 : 2;
    updateReviewCarousel();
    currentReviewPage == 1
      ? (currentReviewPage = 3)
      : (currentReviewPage = currentReviewPage - 1);
    reviewPage.innerText = currentReviewPage;
  });

  btnReviewNext.addEventListener("click", () => {
    currentReviewIndex = currentReviewIndex < 2 ? currentReviewIndex + 1 : 0;
    updateReviewCarousel();
    currentReviewPage == 3
      ? (currentReviewPage = 1)
      : (currentReviewPage = currentReviewPage + 1);

    reviewPage.innerText = currentReviewPage;
  });

  //--------------------------------------------------------
  //淡入淡出動畫
  const observer = new IntersectionObserver(
    (entries) => {
      entries.forEach((entry) => {
        if (entry.isIntersecting) {
          entry.target.classList.add("visible");
        } else {
          entry.target.classList.remove("visible");
        }
      });
    },
    { threshold: 0.3 }
  );
  document.querySelectorAll(".fadeIn").forEach((el) => observer.observe(el));

  //-------------------------------------------------------
  //圖片動畫
  window.addEventListener("scroll", () => {
    const scrollY = window.scrollY;

    const pic2 = document.getElementById("searchPic2");
    const pi2cOffsetX = scrollY * 0.15;
    pic2.style.transform = `translateX(${pi2cOffsetX}px)`;

    const slogan = document.getElementById("slogan");
    const sloganOffsetX = scrollY * 0.3;
    slogan.style.transform = `translateX(${sloganOffsetX}px)`;

    const pic1 = document.getElementById("searchPic1");
    const pic1OffsetX = scrollY * -0.1;
    pic1.style.transform = `translateX(${pic1OffsetX}px)`;

    const pic3 = document.getElementById("searchPic3");
    const pic3OffsetY = scrollY * -0.3;
    pic3.style.transform = `translateY(${pic3OffsetY}px)`;

    const bg = document.getElementById("bg1");
    const bg1OffsetY = scrollY * -0.2;
    bg1.style.transform = `translateY(${bg1OffsetY}px)`;

    const bg2 = document.getElementById("bg2");
    const bg2OffsetY = scrollY * -0.05;
    bg2.style.transform = `translateY(${bg2OffsetY}px)`;
  });

  //-------------------------------------------------------------------------
  //後端
  //繼續學習
  function progress() {
    $.ajax({
      url: "http://localhost:8080/progress/user",
      method: "GET",
      xhrFields: {
        withCredentials: true,
      },
    }).done((data) => {
      console.log(data);
      $.each(data, (idx, item) => {
        if (data.length > 0) {
          $(".Progress").removeClass("hidden");
          if (idx < 4) {
            $("#progressResult").append(
              ` <a href="http://127.0.0.1:5500/pages/lecture.html?course_id=${item.courseId}" class="m-4 mx-auto group">
          <div class=" max-w-72">
          <div class="h-44 w-72 border-2 border-black bg-white overflow-hidden">
              <img
                src="${item.courseImg}"
                class=" object-cover h-full w-full group-hover:scale-105 duration-300"
              />
          </div>
              <p class="text-xl my-2 group-hover:underline">${item.courseName}</p>
              <p class="text-gray-500">${item.teacher}</p>
              <div class="h-3 w-full border-2 rounded-md">
                <div
                  class="h-full bg-indigo-600 rounded-md"
                  style="width: ${item.completedPercentage}%"
                ></div>
              </div>
              ${item.completedPercentage}%完成
            </div>
            </a>`
            );
          }
        }
      });
    });
  }
  fetch("http://localhost:8080/user/islogin", {
    method: "get",
    credentials: "include",
  })
    .then((response) => response.text())
    .then((data) => {
      if (data == "true") {
        progress();
      }
    });

  function url(api) {
    return `http://localhost:8080/index/${api}`;
  }

  function rateToStars(rate) {
    let stars =
      '<i class="bi bi-star-fill"></i><i class="bi bi-star-fill"></i><i class="bi bi-star-fill"></i><i class="bi bi-star-fill"></i><i class="bi bi-star-fill"></i>';
    if (rate < 4.75 && rate >= 4.25) {
      stars =
        '<i class="bi bi-star-fill"></i><i class="bi bi-star-fill"></i><i class="bi bi-star-fill"></i><i class="bi bi-star-fill"></i><i class="bi bi-star-half"></i>';
    } else if (rate < 4.25 && rate >= 3.75) {
      stars =
        '<i class="bi bi-star-fill"></i><i class="bi bi-star-fill"></i><i class="bi bi-star-fill"></i><i class="bi bi-star-fill"></i><i class="bi bi-star"></i>';
    } else if (rate < 3.75 && rate >= 3.25) {
      stars =
        '<i class="bi bi-star-fill"></i><i class="bi bi-star-fill"></i><i class="bi bi-star-fill"></i><i class="bi bi-star-half"></i><i class="bi bi-star"></i>';
    } else if (rate < 3.25 && rate >= 2.75) {
      stars =
        '<i class="bi bi-star-fill"></i><i class="bi bi-star-fill"></i><i class="bi bi-star-fill"></i><i class="bi bi-star"></i><i class="bi bi-star"></i>';
    } else if (rate < 2.75 && rate >= 2.25) {
      stars =
        '<i class="bi bi-star-fill"></i><i class="bi bi-star-fill"></i><i class="bi bi-star-half"></i><i class="bi bi-star"></i><i class="bi bi-star"></i>';
    } else if (rate < 2.25 && rate >= 1.75) {
      stars =
        '<i class="bi bi-star-fill"></i><i class="bi bi-star-fill"></i><i class="bi bi-star"></i><i class="bi bi-star"></i><i class="bi bi-star"></i>';
    } else if (rate < 1.75 && rate >= 1.25) {
      stars =
        '<i class="bi bi-star-fill"></i><i class="bi bi-star-half"></i><i class="bi bi-star"></i><i class="bi bi-star"></i><i class="bi bi-star"></i>';
    } else if (rate < 1.25) {
      stars =
        '<i class="bi bi-star-fill"></i><i class="bi bi-star"></i><i class="bi bi-star"></i><i class="bi bi-star"></i><i class="bi bi-star"></i>';
    } else if (rate == 0 || rate == "n") {
      stars =
        '<i class="bi bi-star"></i><i class="bi bi-star"></i><i class="bi bi-star"></i><i class="bi bi-star"></i><i class="bi bi-star"></i>';
    }
    return stars;
  }
  //課程推薦
  $.ajax({
    url: url("courses"),
    method: "GET",
  }).done((data) => {
    $.each(data, function (idx, item) {
      let href = `/pages/course-details.html?course_id=${item.courseId}`;
      $(`#aCourse${idx + 1}`).prop("href", href);
      $(`#imgCourse${idx + 1}`).prop("src", item.courseImg);
      $(`#nameCourse${idx + 1}`).text(item.courseName);
      $(`#teacherCourse${idx + 1}`).text(item.teacherName);
      $(`#studentsCourse${idx + 1}`).text(item.students);
      $(`#rateCourse${idx + 1}`).html(
        `<span class="text-yellow-400">${rateToStars(
          item.courseRate
        )}</span> (${item.courseRate})`
      );
      $(`#priceCourse${idx + 1}`).text(item.price);
    });
  });
  //評論
  $.ajax({
    url: url("review"),
    method: "GET",
  }).done((data) => {
    $.each(data, function (idx, item) {
      let href = `/pages/course-details.html?course_id=${item.courseId}`;
      $(`#aReview${idx + 1}`).prop("href", href);
      $(`#imgReview${idx + 1}`).prop("src", item.avatar);
      $(`#courseNameReview${idx + 1}`).text(item.courseName);
      $(`#userNameReview${idx + 1}`).text(item.userName);
      $(`#rateReview${idx + 1}`).html(rateToStars(item.rate));
      $(`#review${idx + 1}`).text(item.review);
      $(`#timeReview${idx + 1}`).text(timeCal(item.time));
    });
  });
});
//搜尋
$("#btnSearch").on("click", (event) => {
  event.preventDefault();
  const query = $("#inputSearch").prop("value");
  window.location.href = `pages/search.html?query=${query}`;
});

//AI
//------------------------------------------------------------
CoursesData = "";
function getCourseNameId() {
  $.ajax({
    url: "http://localhost:8080/courses/getAllNameId",
    method: "GET",
  }).done((data) => {
    $.each(data, (idx, item) => {
      CoursesData += `${item.Id}:CourseName:${item.Name},`;
    });
  });
}
getCourseNameId();
async function aiApi(qs) {
  let data = {
    messages: [
      {
        role: "system",
        content: `你是一名線上課程平台的客服人員，網站名稱為EZLEARN，請為顧客服務解決顧客的問題。
    課程主題：語言學習、程式設計、美食料理、藝術創作、運動健身、理財投資
    會員登入：http://127.0.0.1:5500/pages/login.html
    會員註冊：http://127.0.0.1:5500/pages/register.html
    搜尋課程：http://127.0.0.1:5500/pages/search.html?query=
    課程：http://127.0.0.1:5500/pages/course-details.html?course_id=
    課程列表：${CoursesData}
    回覆網址的格式：<a class="underline" href="http://127.0.0.1:5500/pages/register.html" target="_blank">會員註冊</a><br>，
    推薦課程也是以上述的格式
    以下網址登入後才能使用
    訂單紀錄：http://127.0.0.1:5500/pages/order-history.html
    購物車：http://127.0.0.1:5500/pages/cart.html
    通知：http://127.0.0.1:5500/pages/notify.html
    願望清單：http://127.0.0.1:5500/pages/wish-list.html
    我的課程:http://127.0.0.1:5500/pages/my-courses.html
    管理課程：http://127.0.0.1:5500/pages/teacher-mainJQ.html
    地址：台中市南屯區公益路二段51號18樓
    電話：(04) 2326-5860#6541
    EMail：iiispan@ispan.com.tw
    以上是我們網站資訊，
    不要回答顧客關於本網站以外的問題，
    現在開始為user回答問題
    `,
      },
      {
        role: "user",
        content: qs,
      },
    ],
    model: "gpt-4o-mini",
    temperature: 1,
    max_tokens: 4096,
    top_p: 1,
  };

  const response = await fetch("./token.json");
  const headers = await response.json();
  console.log(headers[0]);
  console.log(headers[1]);
  await $.ajax({
    url: "https://models.inference.ai.azure.com/chat/completions",
    method: "POST",
    contentType: "application/json",
    headers: headers[0],
    data: JSON.stringify(data),
  })
    .done((response) => {
      $("#qsResults").append(`
            <div class="mb-4 flex justify-start">
            <img src="./imgs/customer-service.png" class="w-10 h-10 mr-2" />
            <div class="relative max-w-[80%] py-2 px-3 bg-blue-500 text-white rounded-lg border border-blue-500 self-start"               style="
                word-wrap: break-word;
                word-break: break-word;
                white-space: normal;
              ">
              <span>${response.choices[0].message.content}</span>
            </div>
          </div>
`);
      $("#btnQs").prop("disabled", false).text("發送");
    })
    .fail((error) => {
      console.error("Error:", error);
    });
}
function btnHidden() {
  if ($("#divCustomerService").hasClass("hidden")) {
    $("#divCustomerService").removeClass("hidden");
  } else {
    $("#divCustomerService").addClass("hidden");
  }
}
$("#btnCustomerService").on("click", () => {
  $("#needHelp").addClass("hidden");
  btnHidden();
});

$("#btnQs").on("click", () => {
  event.preventDefault();
  let qs = $("#qs").val();
  if (qs == "") {
    return;
  }
  $("#btnQs").prop("disabled", true).text("回覆中");
  $("#qsResults").append(`
            <div class="relative mb-4 max-w-[80%] py-2 px-3 bg-gray-200 text-gray-800 rounded-lg border border-gray-300 self-end"              style="
                word-wrap: break-word;
                word-break: break-word;
                white-space: normal;
              ">
              <span>${qs}</span>
            </div>`);
  aiApi(qs);
  $("#qs").prop("value", "");
});

// 關鍵字搜尋

//防抖
function debounce(fn, delay = 500) {
  let timer;
  return (...args) => {
    clearTimeout(timer);
    timer = setTimeout(() => {
      fn(...args);
    }, delay);
  };
}
let iskeywordNull = true;
let updateDebounceText = debounce((text) => {
  if (text != "") {
    keyword(text);
  } else {
    $("#keyword").addClass("hidden");
    $("#keyword").empty();
    iskeywordNull = true;
  }
}, 700);
let blurHidden = debounce(() => {
  $("#keyword").addClass("hidden");
}, 100);

$("#inputSearch").on("input", function () {
  let searchText = $(this).val();
  updateDebounceText(searchText);
});

$("#inputSearch").on("blur", function () {
  blurHidden();
});

$("#inputSearch").on("focus", function () {
  if (iskeywordNull == false) {
    $("#keyword").removeClass("hidden");
  }
});
async function keyword(key) {
  let data = {
    messages: [
      {
        role: "system",
        content: `你是線上課程平台的搜尋關鍵字推薦，
    課程列表：${CoursesData}
    根據課程列表及使用者輸入的字提供合適的關鍵字，關鍵字前幾個字需與使用者輸入的相同
    一次最多提供五個關鍵字
    你的回覆格式為：<a class='p-2 block hover:bg-gray-100' href='../pages/search.html?query=關鍵字'>關鍵字</a>
    你的回覆只能是我提供的格式，不能有其他文字
    如果使用者輸入的字皆與課程列表無關，你可以隨意提供課程相關的關鍵字但前幾個字也是要跟使用者輸入的相同。
    `,
      },
      {
        role: "user",
        content: key,
      },
    ],
    model: "gpt-4o-mini",
    temperature: 1,
    max_tokens: 4096,
    top_p: 1,
  };

  const response = await fetch("./token.json");
  const headers = await response.json();

  await $.ajax({
    url: "https://models.inference.ai.azure.com/chat/completions",
    method: "POST",
    contentType: "application/json",
    headers: headers[1],
    data: JSON.stringify(data),
  })
    .done((response) => {
      $("#keyword").removeClass("hidden");
      $("#keyword").empty();
      $("#keyword").append(`${response.choices[0].message.content}
        `);
      iskeywordNull = false;
    })
    .fail((error) => {
      console.error("Error:", error);
    });
}
