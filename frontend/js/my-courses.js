$(document).ready(function () {
  async function getMyCourse() {
    $("#course-list").html("");
    try {
      const response = await fetch(
        "http://localhost:8080/purchased-courses/my-courses",
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
        console.log(data);
        $("#browseCourse").addClass("hidden");
        $.each(data, function (index, item) {
          $("#course-list").append(
            `
            <div class="purchased-course border-b border-[#AAAAAA] m-4">
              <a href="/pages/lecture.html?course_id=${
                item.courses.courseId
              }">
                <div class="group relative flex flex-col justify-center items-center cursor-pointer">
                  <img src="data:image/png;base64,${
                    item.courses.courseImg
                  }" class="h-[110px] w-[200px] object-cover">
                  <h1 class="font-bold text-[#2D2F31] mt-2">${
                    item.courses.courseName
                  }</h1>
                  <p class="text-xs text-[#494847]">老師：${
                    item.courses.userInfo.userName
                  }</p>
                  <div class="absolute top-0 h-[110px] w-[200px] bg-black bg-opacity-50 opacity-0 group-hover:opacity-100 transition-opacity duration-300">
                    <i class="bi bi-play-circle-fill absolute top-1/2 left-1/2 text-gray-200 transform -translate-x-1/2 -translate-y-1/2 text-5xl"></i>
                  </div>
                </div>
              </a>
              
              <div class="flex justify-center">
                <button data-cid="${
                  item.courses.courseId
                }" data-name="${
              item.courses.courseName
            }" data-rate="${
              item.courseRate != null ? item.courseRate : 0
            }" data-review="${
              item.courseReview != null
                ? item.courseReview
                : ""
            }" class="review-modal-open text-center text-xs text-indigo-500 hover:text-indigo-700 my-2">${
              item.courseRate != null
                ? "修改評價"
                : "留下評價"
            }</button>
              </div>
            </div>
            `
          );
        });
      } else {
        $("#browseCourse").removeClass("hidden");
      }
    } catch (error) {
      console.log(error);
    }
  }
  getMyCourse();

  let selectedRating = 0;
  let currentRating = 0;
  let courseId = 0;

  $(document).on(
    "click",
    ".review-modal-open",
    function () {
      courseId = $(this).data("cid");
      $(".review-modal").removeClass("hidden");
      $(".course-name").text($(this).data("name"));
      $("#course-review").val($(this).data("review"));
      $(".review-submit-btn").text(
        $(this).data("rate") != 0 ? "修改評價" : "留下評價"
      );

      const selectedRating = $(this).data("rate");
      $(".rate-star").each(function () {
        currentRating = $(this).data("rating");
        if (currentRating <= selectedRating) {
          $(this)
            .find("i")
            .removeClass("bi-star")
            .addClass("bi-star-fill text-[#F69C08]");
        } else {
          $(this)
            .find("i")
            .removeClass("bi-star-fill text-[#F69C08]")
            .addClass("bi-star");
        }
      });
    }
  );

  $(".review-submit-btn").on("click", async function () {
    try {
      const response = await fetch(
        `http://localhost:8080/purchased-courses/${courseId}/review`,
        {
          method: "PUT",
          credentials: "include",
          headers: {
            "Content-Type": "application/json",
          },
          body: JSON.stringify({
            courseRate: selectedRating
              ? selectedRating
              : currentRating,
            courseReview: $("#course-review").val(),
          }),
        }
      );

      if (!response.ok) {
        throw new Error("Internal Error");
      } else {
        $(".review-modal").addClass("hidden");
        getMyCourse();
      }
    } catch (error) {
      console.log(error);
    }
  });

  $(document).on("click", ".close-modal-btn", function () {
    $(".review-modal").addClass("hidden");
  });

  // review and rate
  $(".rate-star").on("click", function () {
    selectedRating = $(this).data("rating");

    $(".rate-star").each(function () {
      const currentRating = $(this).data("rating");
      if (currentRating <= selectedRating) {
        $(this)
          .find("i")
          .removeClass("bi-star")
          .addClass("bi-star-fill text-[#F69C08]");
      } else {
        $(this)
          .find("i")
          .removeClass("bi-star-fill text-[#F69C08]")
          .addClass("bi-star");
      }
    });

    console.log(selectedRating);
  });
});
