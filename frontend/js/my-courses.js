$(document).ready(function () {
  async function getMyCourse() {
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
          let courseImg = $("<img>")
            .attr(
              "src",
              `data:image/png;base64,${item.courses.courseImg}`
            )
            .addClass("h-[110px] w-[200px] object-cover");

          let courseName = $("<h1>")
            .text(item.courses.courseName)
            .addClass("font-bold text-[#2D2F31] mt-2");

          let teacherName = $("<p>")
            .text(`老師：${item.courses.userInfo.userName}`)
            .addClass("text-xs text-[#494847] mb-2");

          let playIcon = $("<i>").addClass(
            "bi bi-play-circle-fill absolute top-1/2 left-1/2 text-gray-200 transform -translate-x-1/2 -translate-y-1/2 text-5xl"
          );
          let mask = $("<div>")
            .addClass(
              "absolute top-0 h-[110px] w-[200px] bg-black bg-opacity-50 opacity-0 group-hover:opacity-100 transition-opacity duration-300"
            )
            .append(playIcon);

          let courseCard = $("<div>")
            .addClass(
              "group relative flex flex-col justify-center items-center m-4 cursor-pointer border-b border-[#AAAAAA]"
            )
            .append(
              courseImg,
              courseName,
              teacherName,
              mask
            );
          $("#course-list").append(courseCard);
        });
      } else {
        $("#browseCourse").removeClass("hidden");
      }
      console.log(data);
    } catch (error) {
      console.log(error);
    }
  }

  getMyCourse();
});
