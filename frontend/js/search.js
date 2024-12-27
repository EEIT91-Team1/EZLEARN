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
option("review");
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
