//願望清單
async function addWishList(id) {
  if ($(`#wish${id}`).hasClass("isWish")) {
    const url = `http://localhost:8080/wishList/delete?courseId=${id}`;
    await $.ajax({
      url: url,
      method: "POST",
      xhrFields: {
        withCredentials: true,
      },
    });
  } else {
    const url = `http://localhost:8080/wishList/add?courseId=${id}`;
    await $.ajax({
      url: url,
      method: "POST",
      xhrFields: {
        withCredentials: true,
      },
    }).done((data) => {
      if (data == "isTeacher") {
        alert("無法將自己的課程加入願望清單");
      }
    });
  }
  loadResults();
}

//篩選列隱藏
function option(title) {
  $(`#${title}`).on("click", () => {
    if ($(`#${title}Option`).hasClass("hidden")) {
      $(`#${title}Option`).removeClass("hidden");
      $(`#${title}`).find("i").removeClass("bi-chevron-down");
      $(`#${title}`).find("i").addClass("bi-chevron-up");
    } else {
      $(`#${title}Option`).addClass("hidden");
      $(`#${title}`).find("i").addClass("bi-chevron-down");
      $(`#${title}`).find("i").removeClass("bi-chevron-up");
    }
  });
}
option("theme");
option("rate");
option("price");
//-------------------------------------------------------------
//Div隱藏顯示
function show(btn, view) {
  $(btn).on("click", () => {
    if (view.hasClass("hidden")) {
      view.removeClass("hidden");
    } else {
      view.addClass("hidden");
    }
  });
}
show($("#sort"), $("#sortOption"));
show($("#filter"), $("#divLeft"));
show($("#closeDivLeft"), $("#divLeft"));
//------------------------------------------------------------
//圖片動畫
window.addEventListener("scroll", () => {
  const scrollY = window.scrollY;

  const pic4 = document.getElementById("searchPic4");
  const pi4cOffsetX = scrollY * -0.4;
  pic4.style.transform = `translateX(${pi4cOffsetX}px)`;

  const pic6 = document.getElementById("searchPic6");
  const pic6OffsetX = scrollY * 0.05;
  pic6.style.transform = `translateX(${pic6OffsetX}px)`;
});
//------------------------------------------------------------
//搜尋框
$("#btnSearch").on("click", (event) => {
  event.preventDefault();
  console.log("a");
  const query = $("#inputSearch").prop("value");
  window.location.href = `search.html?query=${query}`;
});
//------------------------------------------------------------
//後端
//設定參數
function getSearchParams() {
  let urlParams = new URLSearchParams(window.location.search);
  let query = urlParams.get("query") || "";
  let page = urlParams.get("page") ? "&page=" + urlParams.get("page") : "";
  let type = urlParams.get("type") ? "&type=" + urlParams.get("type") : "";
  let price = urlParams.get("price") ? "&price=" + urlParams.get("price") : "";
  let rate = urlParams.get("rate") ? "&rate=" + urlParams.get("rate") : "";
  let sort = urlParams.get("sort") ? "&sort=" + urlParams.get("sort") : "";
  let pageNumber = urlParams.get("page") ? urlParams.get("page") : "1";
  return {
    query,
    page,
    type,
    price,
    rate,
    sort,
    pageNumber,
  };
}
function setUrl(url, params) {
  return `http://localhost:8080/search/${url}?query=${params.query}${params.page}${params.type}${params.price}${params.rate}${params.sort}`;
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
let hasNextPage = false;

//結果列
async function loadResults() {
  hasNextPage = true;
  const params = getSearchParams();
  const url = setUrl("search", params);
  $("#pageNumber").text(params.pageNumber);
  $("#divRight").empty();

  await $.ajax({
    url: url,
    method: "GET",
  }).done((data) => {
    if (data.length <= 10) {
      hasNextPage = false;
    }
    $.each(data, function (idx, item) {
      if (idx < 10) {
        let href = `/pages/course-details.html?course_id=${item.courseId}`;
        $("#divRight").append(
          `<div class="relative z-0 group">
            <a  href="${href}">
            <div class="divResult group-hover:bg-gray-200 duration-300">
             <div class="divImg">
               <img src="${
                 item.courseImg
               }" class="duration-300 group-hover:scale-105"/>
             </div>
             <div class="divText">
               <h1>${item.courseName}</h1>
               <h2>${item.courseIntro}</h2>
               <h3>${item.teacherName}</h3>
               <h4><span class="text-yellow-400">${rateToStars(
                 item.courseRate
               )}</span>（${item.courseRate}）</h4>
             </div>
             <div class="divPrice">
               <p>$${item.price}</p>
               </div>
          </div></a>
               <div class="text-4xl text-end absolute bottom-4 right-8 z-10">
               <i onclick="addWishList(${item.courseId})" id="wish${
            item.courseId
          }" class="bi bi-heart-fill text-gray-100 hover:text-red-400 duration-300 mr-2 cursor-pointer"></i>
               </div>
          </div>`
        );
      }
    });
  });

  await $.ajax({
    url: "http://localhost:8080/wishList/get",
    method: "GET",
    xhrFields: {
      withCredentials: true,
    },
  }).done((data) => {
    if (data != 0) {
      $.each(data, function (idx, item) {
        $(`#wish${item.courseId}`).css("color", "rgb(248 113 113)");
        $(`#wish${item.courseId}`).addClass("isWish");
      });
    }
  });
}
//查詢筆數
function getCount() {
  const params = getSearchParams();
  const url = setUrl("searchCount", params);
  $.ajax({
    url: url,
    method: "GET",
  }).done((data) => {
    if (params.query == "") {
      $("#queryText").text(`「所有分類」有 ${data.courseCount} 個結果`);
    } else {
      $("#queryText").text(`「${params.query}」有 ${data.courseCount} 個結果`);
    }
    $("#languageLabel").text(`(${data.languageCount})`);
    $("#programLabel").text(`(${data.programCount})`);
    $("#cookLabel").text(`(${data.cookCount})`);
    $("#artLabel").text(`(${data.artCount})`);
    $("#sportLabel").text(`(${data.sportCount})`);
    $("#financeLabel").text(`(${data.financeCount})`);
  });
}

getCount();
loadResults();

//-------------------------------------------------------------
//type
$("input[name ='type']").change(function () {
  let typeValue = $(this).val();
  const urlParams = new URLSearchParams(window.location.search);
  urlParams.delete("page");
  if (typeValue === "all") {
    urlParams.delete("type");
  } else {
    urlParams.set("type", typeValue);
  }
  history.replaceState(null, "", "?" + urlParams.toString());
  loadResults();
});
//-------------------------------------------------------------
//rate
$("input[name ='rate']").change(function () {
  let rateValue = $(this).val();
  const urlParams = new URLSearchParams(window.location.search);
  urlParams.delete("page");
  if (rateValue === "all") {
    urlParams.delete("rate");
  } else {
    urlParams.set("rate", rateValue);
  }
  history.replaceState(null, "", "?" + urlParams.toString());
  loadResults();
});
//------------------------------------------------------------
//Price
$('input[type="radio"][name="price"]').on("change", function () {
  if ($(this).val() === "choose") {
    $("#divPriceRange").removeClass("hidden");
  } else {
    $("#divPriceRange").addClass("hidden");
    $("#inputPrice").val("");
    $("#showPrice").text("");
    let priceValue = $("#inputPrice").val();
    const urlParams = new URLSearchParams(window.location.search);
    urlParams.delete("page");
    urlParams.delete("price");
    history.replaceState(null, "", "?" + urlParams.toString());
    loadResults();
  }
});
$("#inputPrice").on("input", () => {
  $("#showPrice").text($("#inputPrice").val() + "元以下");
});
$("#inputPrice").change(() => {
  let priceValue = $("#inputPrice").val();
  const urlParams = new URLSearchParams(window.location.search);
  urlParams.delete("page");
  if (priceValue === "all") {
    urlParams.delete("price");
  } else {
    urlParams.set("price", priceValue);
  }
  history.replaceState(null, "", "?" + urlParams.toString());
  loadResults();
});
//-----------------------------------------------------------------
//排序
function sort() {
  let urlParams = new URLSearchParams(window.location.search);
  switch (urlParams.get("sort")) {
    case "rate":
      $("#sortRate").addClass("sortTarget");
      break;
    case "students":
      $("#sortStudents").addClass("sortTarget");
      break;
    case "pb":
      $("#sortPriceB").addClass("sortTarget");
      break;
    case "pt":
      $("#sortPriceT").addClass("sortTarget");
    default:
      $("#sortNew").addClass("sortTarget");
      break;
  }
}
sort();
function sortChoose(param, val) {
  param.click(function () {
    $("a.sort").removeClass("sortTarget");
    $(this).addClass("sortTarget");
    let sortValue = val;
    const urlParams = new URLSearchParams(window.location.search);
    urlParams.delete("page");
    urlParams.set("sort", sortValue);
    history.replaceState(null, "", "?" + urlParams.toString());
    loadResults();
  });
}
sortChoose($("#sortRate"), "rate");
sortChoose($("#sortStudents"), "students");
sortChoose($("#sortPriceT"), "pt");
sortChoose($("#sortPriceB"), "pb");

$("#sortNew").click(function () {
  $("a.sort").removeClass("sortTarget");
  $(this).addClass("sortTarget");
  const urlParams = new URLSearchParams(window.location.search);
  urlParams.delete("page");
  urlParams.delete("sort");
  history.replaceState(null, "", "?" + urlParams.toString());
  loadResults();
});
//-----------------------------------------------------------------
//頁數
$("#btnPageNext").click(() => {
  const params = getSearchParams();
  if (hasNextPage == false) {
    alert("已經到最後一頁囉");
  } else {
    const urlParams = new URLSearchParams(window.location.search);
    urlParams.set("page", parseInt(params.pageNumber) + 1);
    history.replaceState(null, "", "?" + urlParams.toString());
    loadResults();
    $("html, body").animate({ scrollTop: 0 });
  }
});
$("#btnPagePrev").click(() => {
  const params = getSearchParams();
  if (parseInt(params.pageNumber) == 1) {
    alert("你已經在第一頁了");
  } else {
    const urlParams = new URLSearchParams(window.location.search);
    urlParams.set("page", parseInt(params.pageNumber) - 1);
    history.replaceState(null, "", "?" + urlParams.toString());
    loadResults();
    $("html, body").animate({ scrollTop: 0 });
  }
});
