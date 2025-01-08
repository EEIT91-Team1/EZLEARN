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

  let courseId = new URLSearchParams(
    window.location.search
  ).get("course_id");

  //get course review and course rate
  async function getPurchasedCourses() {
    try {
      const response = await fetch(
        `http://localhost:8080/purchased-courses/${courseId}`,
        {
          method: "GET",
          credentials: "include",
          headers: {
            "Content-Type": "application/json",
          },
        }
      );

      if (!response.ok) {
        throw new Error("Internal Error");
      }

      const data = await response.json();
      if (data.length > 0) {
        $.each(data, function (index, item) {
          $(".course-review").append(`<div
          class="flex items-center mr-8 border-b border-gray-300 p-4"
        >
          <div class="mr-4 w-24 h-24">
            <img
              class="rounded-full w-full h-full object-cover"
              src="data:image/png;base64,${item.users.userInfo.avatar}"
              alt=""
            />
          </div>
          <div>
            <p class="text-[14px]">
              <span class="rate-star-${index} text-[#F69C08]">
              </span>
            </p>
            <p class="font-bold text-[#212529]">
              ${item.users.userInfo.userName}
            </p>
            <p class="text-[#495057]">
              ${item.courseReview}
            </p>
          </div>
        </div>`);
          renderStars(item.courseRate, index);
        });
      } else {
        $(".course-review").append(
          `<p class="text-[#495057]">此課程尚未有評論</p>`
        );
      }
    } catch (error) {
      console.log(error);
    }
  }
  getPurchasedCourses();

  //get course info
  async function getCourseDetails() {
    try {
      const response = await fetch(
        `http://localhost:8080/courses/${courseId}`,
        {
          method: "GET",
          credentials: "include",
          headers: {
            "Content-Type": "application/json",
          },
        }
      );

      if (!response.ok) {
        throw new Error("Internal Error");
      }

      const data = await response.json();
      $(".course-name").text(data.courseName);
      $(".course-summary").text(data.courseSummary);
      $(".course-intro").text(data.courseIntro);
      $(".course-img").attr(
        "src",
        `data:image/png;base64,${data.courseImg}`
      );
      $(".user-name").text(data.userInfo.userName);
      $(".avatar").attr(
        "src",
        `data:image/png;base64,${data.userInfo.avatar}`
      );
      $(".user-intro").text(data.userInfo.userIntro);
      $(".price").text(data.price);
      $(".updated-at").text(data.updatedAt);
      $("title").text(`${data.courseName} | EZLËARN`);
    } catch (error) {
      console.log(error);
    }
  }
  getCourseDetails();

  function renderStars(rating, index) {
    $(`.rate-star-${index}`).empty();

    const fullStars = Math.floor(rating);
    const halfStar = rating % 1 >= 0.5 ? 1 : 0;
    const emptyStars = 5 - fullStars - halfStar;

    for (let i = 0; i < fullStars; i++) {
      $(`.rate-star-${index}`).append(
        '<i class="bi bi-star-fill"></i>'
      );
    }

    if (halfStar) {
      $(`.rate-star-${index}`).append(
        '<i class="bi bi-star-half"></i>'
      );
    }

    for (let i = 0; i < emptyStars; i++) {
      $(`.rate-star-${index}`).append(
        '<i class="bi bi-star"></i>'
      );
    }
  }

  //get average course rate
  async function getAverageRateForCourse() {
    try {
      const response = await fetch(
        `http://localhost:8080/purchased-courses/${courseId}/average-rate`,
        {
          method: "GET",
          credentials: "include",
          headers: {
            "Content-Type": "application/json",
          },
        }
      );

      if (!response.ok) {
        throw new Error("Internal Error");
      }

      const data = await response.json();
      $(".course-rate").text(parseFloat(data).toFixed(1));

      renderStars(data, "avg");
    } catch (error) {
      console.log(error);
    }
  }
  getAverageRateForCourse();
});
