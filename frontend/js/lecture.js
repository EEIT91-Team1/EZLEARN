$(document).ready(function () {
  // switch tab
  $(".tab-link").on("click", function (e) {
    e.preventDefault();

    $(".tab-content").addClass("hidden");

    $(".tab-link").removeClass(
      "border-indigo-500 text-indigo-600"
    );
    $(".tab-link").addClass(
      "border-transparent text-gray-500"
    );

    var target = $(this).data("target");
    $("#" + target).removeClass("hidden");

    $(this).removeClass("border-transparent text-gray-500");
    $(this).addClass("border-indigo-500 text-indigo-600");
  });

  let courseId = new URLSearchParams(
    window.location.search
  ).get("course_id");

  let lessonId = 0;
  let currentLesson = "";

  // get course announcement by course id
  async function getPosts() {
    try {
      const response = await fetch(
        `http://localhost:8080/courses/${courseId}/posts`,
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
        $("#no-posts").addClass("hidden");
        $.each(data, function (index, item) {
          let postTitle = $("<h1>")
            .text(item.postTitle)
            .addClass(
              "text-lg font-bold mb-2 text-gray-800"
            );

          let postContent = $("<p>")
            .text(item.postContent)
            .addClass("text-gray-600");

          let dateTime = $("<p>")
            .text(item.createdAt)
            .addClass("text-xs text-end text-gray-600");

          let postBox = $("<div>")
            .addClass("border border-gray-400 p-4 my-2")
            .append(postTitle, postContent, dateTime);
          $("#tab-1").append(postBox);
        });
      } else {
        $("#no-posts").removeClass("hidden");
      }
    } catch (error) {
      console.log(error);
    }
  }
  getPosts();

  //get note by course id
  async function getNotes() {
    $(".note-list").empty();
    try {
      const response = await fetch(
        `http://localhost:8080/courses/${courseId}/notes`,
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

      $.each(data, function (index, item) {
        $(".note-list").prepend(`<div
        id="${item.noteId}"
        class="note border border-gray-400 p-4 my-2 relative"
      >
        <div class="absolute right-2 top-2">
          <div class="relative flex">
            <button
              class="note-edit-btn group p-2 text-gray-700"
            >
              <i
                class="text-xl bi bi-pencil-square group-hover:text-gray-500"
              ></i>
            </button>

            <button
              
              class="note-delete-btn group p-2 text-gray-700"
            >
              <i
                class="text-xl bi bi-trash group-hover:text-gray-500"
              ></i>
            </button>
          </div>
        </div>

        <textarea
          class="note-title disabled:bg-white block w-[90%] h-[36px] resize-none text-lg font-bold text-gray-700 mb-4 focus:outline focus:outline-0"
          disabled
        >${item.noteTitle}</textarea>

        <textarea
          class="note-content disabled:bg-white block w-full resize-none  text-gray-700 placeholder:text-gray-400 focus:outline focus:outline-0"
          disabled
        >${item.noteContent}</textarea>
          <p class="text-xs text-end text-gray-600">${item.lessons.lessonName}－${item.updatedAt}</p>
      </div>`);
      });
    } catch (error) {
      console.log(error);
    }
  }
  getNotes();

  // add note
  async function addNote() {
    const response = await fetch(
      `http://localhost:8080/courses/notes`,
      {
        method: "POST",
        credentials: "include",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({
          noteTitle: $("#add-note-title").val(),
          noteContent: $("#add-note-content").val(),
          lessons: {
            lessonId: lessonId,
          },
        }),
      }
    );

    if (!response.ok) {
      throw new Error("Internal Error");
    }

    getNotes();
  }
  $("#add-note-btn").on("click", function () {
    addNote();
    $("#add-note-title").val("");
    $("#add-note-content").val("");
  });

  // delete note
  $(document).on(
    "click",
    ".note-delete-btn",
    async function (e) {
      let noteId = $(this).closest(".note").attr("id");
      $(this).closest(".note").remove();
      await fetch(
        `http://localhost:8080/courses/notes/${noteId}`,
        {
          method: "DELETE",
          credentials: "include",
          headers: {
            "Content-Type": "application/json",
          },
        }
      );
    }
  );

  // edit note
  $(document).on(
    "click",
    ".note-edit-btn",
    async function () {
      if ($(this).find("i").hasClass("bi-floppy")) {
        let noteId = $(this).closest(".note").attr("id");
        const response = await fetch(
          `http://localhost:8080/courses/notes/${noteId}`,
          {
            method: "PUT",
            credentials: "include",
            headers: {
              "Content-Type": "application/json",
            },
            body: JSON.stringify({
              noteTitle: $(this)
                .closest(".note")
                .find(".note-title")
                .val(),

              noteContent: $(this)
                .closest(".note")
                .find(".note-content")
                .val(),
            }),
          }
        );
      }

      $(this)
        .find("i")
        .toggleClass("bi-pencil-square bi-floppy");

      $(this)
        .closest(".note")
        .find(".note-title")
        .toggleClass("text-gray-700 text-gray-400")
        .prop(
          "disabled",
          !$(this)
            .closest(".note")
            .find(".note-title")
            .prop("disabled")
        )
        .focus();

      $(this)
        .closest(".note")
        .find(".note-content")
        .toggleClass("text-gray-700 text-gray-400")
        .prop(
          "disabled",
          !$(this)
            .closest(".note")
            .find(".note-content")
            .prop("disabled")
        );
    }
  );

  // get course info
  async function getCourse() {
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
  }
  getCourse();

  // get lessons
  async function getLessons() {
    const response = await fetch(
      `http://localhost:8080/courses/${courseId}/lessons`,
      {
        method: "GET",
        credentials: "include",
        headers: {
          "Content-Type": "application/json",
        },
      }
    );

    if (
      response.status === 401 ||
      response.status === 400
    ) {
      window.location.href = "/pages/login.html";
    }

    if (!response.ok) {
      throw new Error("Internal Error");
    }

    const data = await response.json();
    if (data.length > 0) {
      lessonId = data[0].lessonId; //default lessonId
      $("#courseVideo").attr("src", data[0].videoUrl); // default video
      $(".lesson-name").text(data[0].lessonName); // default lesson

      $.each(data, function (index, item) {
        $(
          ".lesson-list"
        ).append(`<li class="border-b border-gray-400">
          <details
            class="group [&_summary::-webkit-details-marker]:hidden"
            open
          >
            <summary
              class="flex cursor-pointer items-center justify-between rounded-lg px-4 py-4 my-2 text-gray-500 hover:bg-gray-100 hover:text-gray-700"
            >
              <span class="text-md font-medium">
                章節 ${index + 1}
              </span>

              <span
                class="shrink-0 transition duration-300 group-open:-rotate-180"
              >
                <svg
                  xmlns="http://www.w3.org/2000/svg"
                  class="size-5"
                  viewBox="0 0 20 20"
                  fill="currentColor"
                >
                  <path
                    fill-rule="evenodd"
                    d="M5.293 7.293a1 1 0 011.414 0L10 10.586l3.293-3.293a1 1 0 111.414 1.414l-4 4a1 1 0 01-1.414 0l-4-4a1 1 0 010-1.414z"
                    clip-rule="evenodd"
                  />
                </svg>
              </span>
            </summary>

            <ul class="mt-2 space-y-1 px-4">
              <li>
                <div
                  data-id="${item.lessonId}"
                  data-src="${item.videoUrl}"
                  class="course-video-link flex items-center cursor-pointer w-full text-start rounded-lg px-4 py-2 my-2 text-sm font-medium text-gray-500 hover:bg-gray-100 hover:text-gray-700"
                >
                  <p>${item.lessonName}</p>
                </div>
              </li>
            </ul>
          </details>
        </li>`);
      });

      $.each(
        $(`[data-id="${lessonId}"]`),
        function (index, item) {
          $(item).addClass("bg-gray-100 text-gray-700");
          $(item).prepend(
            `<img class="playing-gif w-4 h-4 mr-2" src="../imgs/playing.gif" class="w-4 h-4 mr-2">`
          );
        }
      );
    }
  }
  getLessons();

  $(document).on(
    "click",
    ".course-video-link",
    function () {
      lessonId = $(this).data("id"); // change to current lessonId
      currentLesson =
        $(this)
          .closest("details")
          .find("summary span:first")
          .text() + this.innerText;

      $(".lesson-name").text(this.innerText);

      $(".course-video-link").removeClass(
        "bg-gray-100 text-gray-700"
      );
      $(".course-video-link img").remove();

      const videoSrc = $(this).data("src");
      $("#courseVideo").attr("src", videoSrc);

      $.each(
        $(`[data-id="${lessonId}"]`),
        function (index, item) {
          $(item).addClass("bg-gray-100 text-gray-700");
          $(item).prepend(
            `<img class="playing-gif w-4 h-4 mr-2" src="../imgs/playing.gif" class="w-4 h-4 mr-2">`
          );
        }
      );
    }
  );

  //get profile
  async function getProfile() {
    const response = await fetch(
      `http://localhost:8080/user/getprofile`,
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
    $(".avatar").attr(
      "src",
      `data:image/png;base64,${data.userInfo.avatar}`
    );
  }
  getProfile();

  async function getQuestions() {
    const response = await fetch(
      `http://localhost:8080/questions/courses/${courseId}`,
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
    console.log(data);
  }
  getQuestions();

  $(".tab-link").on("click", function (e) {});

  // 開啟 Slide Over
  $("#openDrawer").on("click", function () {
    $("#drawer").removeClass("hidden");
    $("#backdrop")
      .removeClass("opacity-0")
      .addClass("opacity-100");

    $("#drawerPanel")
      .removeClass("translate-x-full")
      .addClass("translate-x-0");
  });

  // 關閉 Slide Over
  $("#closeDrawer").on("click", function () {
    $("#drawer").addClass("hidden");
    $("#backdrop")
      .removeClass("opacity-100")
      .addClass("opacity-0");

    $("#drawerPanel")
      .removeClass("translate-x-0")
      .addClass("translate-x-full");
  });
});
