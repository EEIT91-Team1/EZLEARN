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
          if (item.courseRate != null) {
            $(".course-review").prepend(`<div
            class="flex items-center mr-8 border-b border-gray-300 p-4"
          >
            <div class="mr-4 w-24 h-24">
              <img
                class="rounded-full w-full h-full object-cover"
                src="data:image/png;base64,${
                  item.users.userInfo.avatar
                }"
                alt=""
                onerror="this.onerror=null; this.src='../imgs/12-1.png';"
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
                ${
                  item.courseReview
                    ? item.courseReview
                    : "　"
                }
              </p>
            </div>
          </div>`);
            renderStars(item.courseRate, index);
          }
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

  async function isInWishlist() {
    try {
      const response = await fetch(
        `http://localhost:8080/wishList/get`,
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
      const result = $.grep(data, function (item) {
        return item.courseId == courseId;
      });
      if (result.length > 0) {
        $(".add-to-wish-list")
          .find("i")
          .addClass("bi-heart-fill");
        return true;
      } else {
        $(".add-to-wish-list")
          .find("i")
          .addClass("bi-heart");
        return false;
      }
    } catch (error) {
      console.log(error);
    }
  }
  isInWishlist();

  //add to wish list or cancel
  $(".add-to-wish-list").on("click", async function () {
    if (
      $(".add-to-wish-list").find("i").hasClass("bi-heart")
    ) {
      try {
        const response = await fetch(
          `http://localhost:8080/wishList/add?courseId=${courseId}`,
          {
            method: "POST",
            credentials: "include",
            headers: {
              "Content-Type": "application/json",
            },
          }
        );

        if (!response.ok) {
          window.location.href = "/pages/login.html";
          throw new Error("Internal Error");
        }

        $(this)
          .find("i")
          .toggleClass("bi-heart bi-heart-fill");
      } catch (error) {
        console.log(error);
      }
    } else {
      try {
        const response = await fetch(
          `http://localhost:8080/wishList/delete?courseId=${courseId}`,
          {
            method: "POST",
            credentials: "include",
            headers: {
              "Content-Type": "application/json",
            },
          }
        );

        if (!response.ok) {
          throw new Error("Internal Error");
        }

        $(this)
          .find("i")
          .toggleClass("bi-heart bi-heart-fill");
      } catch (error) {
        console.log(error);
      }
    }
  });

  $(".add-to-cart").on("click", async function () {
    try {
      const response = await fetch(
        `http://localhost:8080/api/cart?courseId=${courseId}`,
        {
          method: "POST",
          credentials: "include",
          headers: {
            "Content-Type": "application/json",
          },
        }
      );

      if (response.status === 401) {
        window.location.href = "/pages/login.html";
      }

      if (response.status == 400) {
        // 購物車內已存在商品
        window.location.href = "/pages/cart.html";
      }
      if (!response.ok) {
        throw new Error("Internal Error");
      } else {
        window.location.href = "/pages/cart.html";
      }
    } catch (error) {
      console.log(error);
    }
  });

  $(".buy-now").on("click", async function () {
    try {
      const response = await fetch(
        `http://localhost:8080/api/cart?courseId=${courseId}`,
        {
          method: "POST",
          credentials: "include",
          headers: {
            "Content-Type": "application/json",
          },
        }
      );
      if (!response.ok) {
        throw new Error("Internal Error");
      }
    } catch (error) {
      console.log(error);
    }

    try {
      const response = await fetch(
        `http://localhost:8080/api/checkout/create`,
        {
          method: "POST",
          credentials: "include",
          headers: {
            "Content-Type": "application/json",
          },
          body: JSON.stringify({
            courseIds: [courseId],
          }),
        }
      );

      if (response.status === 401) {
        window.location.href = "/pages/login.html";
      }

      if (!response.ok) {
        throw new Error("Internal Error");
      }

      const result = await response.json();
      if (result.code === 200) {
        window.location.href = `order.html?orderId=${result.data.orderId}`;
      } else {
        alert("伺服器錯誤：" + result.message);
      }
    } catch (error) {
      console.log(error);
    }
  });

  //isPurchased ? watch purchased course
  async function isPurchasedCourse() {
    try {
      const response = await fetch(
        `http://localhost:8080/purchased-courses/my-courses`,
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
      const result = $.grep(data, function (item) {
        return item.courses.courseId == courseId;
      });
      if (result.length > 0) {
        $(".watch-course")
          .html(`<a href="http://127.0.0.1:5500/pages/lecture.html?course_id=${courseId}" class="inline-flex w-full items-center justify-center gap-x-2 rounded-md bg-rose-300 px-3.5 py-2.5 text-sm font-semibold transition-all text-white shadow-sm hover:bg-rose-400">
  開始觀看
  <svg class="-mr-0.5 size-5" viewBox="0 0 20 20" fill="currentColor" aria-hidden="true" data-slot="icon">
    <path fill-rule="evenodd" d="M10 18a8 8 0 1 0 0-16 8 8 0 0 0 0 16Zm3.857-9.809a.75.75 0 0 0-1.214-.882l-3.483 4.79-1.88-1.88a.75.75 0 1 0-1.06 1.061l2.5 2.5a.75.75 0 0 0 1.137-.089l4-5.5Z" clip-rule="evenodd" />
  </svg>
</a>`);
      }
    } catch (error) {
      console.log(error);
    }
  }
  isPurchasedCourse();

  async function getPreviewLesson() {
    const response = await fetch(
      `http://localhost:8080/courses/${courseId}/preview`,
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

    $("#previewVideo").attr("src", data.videoUrl);
  }
  getPreviewLesson();
});
