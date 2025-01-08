$(document).ready(function () {
  $(".coursePreviewImg").on("click", function () {
    $("#coursePreviewModal").removeClass("hidden");
  });

  $("#modal-bg").on("click", function () {
    $("#coursePreviewModal").addClass("hidden");

    let previewVideo = $("#previewVideo")[0];
    if (previewVideo) {
      previewVideo.pause();
      previewVideo.currentTime = 0;
    }
  });

  $(".tab-link").on("click", function (e) {
    //e.preventDefault();

    $(".tab-link").removeClass(
      "border-indigo-500 text-indigo-600"
    );
    $(".tab-link").addClass(
      "border-transparent text-gray-500"
    );

    $(this).removeClass("border-transparent text-gray-500");
    $(this).addClass("border-indigo-500 text-indigo-600");
  });

  $(window).on("scroll resize", function () {
    let scrollTop = $(window).scrollTop();
    let footerTop = $("#footer").offset().top;
    let windowHeight = $(window).height();

    if ($(window).scrollTop() > 80) {
      $("#productCard")
        .addClass("fixed top-6")
        .removeClass("absolute top-4");
      if (scrollTop + windowHeight - 68 >= footerTop) {
        $("#productCard")
          .removeClass("top-6")
          .css(
            "bottom",
            `${scrollTop + windowHeight - footerTop}px`
          );
      } else {
        $("#productCard")
          .css("bottom", "")
          .addClass("top-6");
      }
    } else {
      $("#productCard")
        .removeClass("fixed top-6")
        .addClass("absolute top-4");
    }
  });
});

$("#user-menu-button").click(function (event) {
  event.stopPropagation(); // 防止事件冒泡，這樣點擊按鈕不會觸發 document 的點擊事件
  $(this).next('[role="menu"]').toggleClass("hidden"); // 切換隱藏/顯示菜單
});

// 當用戶點擊頁面其他地方時，隱藏下拉菜單
$(document).click(function (event) {
  if (
    !$(event.target).closest(
      '#user-menu-button, [role="menu"]'
    ).length
  ) {
    $('[role="menu"]').addClass("hidden"); // 隱藏所有的下拉菜單
  }
});
