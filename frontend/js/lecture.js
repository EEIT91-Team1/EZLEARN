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
  let userId = 0;
  let currentLesson = "";
  let isCompleted = false;

  async function getUser() {
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
    userId = data.userId;
  }
  getUser();

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
    $(".lesson-list").empty();
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
      getLastView();

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
              <span class="lesson-index text-md font-medium">
                章節 ${
                  index + 1
                }<i class="bi bi-check-square-fill ml-2 hidden text-yellow-500"></i>

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
                  <i class="bi bi-check-square-fill ml-2 hidden text-yellow-500"></i>
                </div>
              </li>
            </ul>
          </details>
        </li>`);
      });
    }
  }
  getLessons();

  $(document).on(
    "click",
    ".course-video-link",
    function () {
      lessonId = $(this).data("id"); // change to current lessonId
      getProgressByLessonId(); // get progress

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

  //add question
  async function addQuestion() {
    const response = await fetch(
      `http://localhost:8080/questions/create`,
      {
        method: "POST",
        credentials: "include",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({
          question: $("#student-question").val(),
          lesson: {
            lessonId: lessonId,
          },
        }),
      }
    );

    if (!response.ok) {
      throw new Error("Internal Error");
    } else {
      getQuestions();
    }
  }
  $("#add-question").on("click", function () {
    addQuestion();
    $("#student-question").val("");
    $(".auto-resize").height("auto");
  });

  async function getQuestions() {
    $(".question-list").empty();
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
    if (data.length > 0) {
      $.each(data, function (index, item) {
        $(".question-list").prepend(`<div
                    id="${item.questionId}"
                    class="question flex items-start space-x-4 relative mb-8"
                  >
                    <!-- avatar -->
                    <div class="shrink-0">
                      <img
                        class="avatar inline-block size-10 rounded-full object-cover"
                        src="data:image/png;base64,${
                          item.userInfo.avatar
                        }"
                      />
                    </div>

                    <!-- Q&A -->
                    <div class="min-w-0 flex-1">
                      <div class="flex">
                        <p
                          class="text-xs font-bold text-gray-700"
                        >
                          ${item.userInfo.userName}
                        </p>
                        <span class="text-xs text-gray-700"
                          >　${item.updatedAt}</span
                        >
                      </div>

                      <!-- question -->
                      <div
                        class="pb-px focus-within:border-b-2 focus-within:border-indigo-600 focus-within:pb-0"
                      >
                        <textarea
                          class="auto-resize overflow-y-hidden block w-full resize-none bg-white text-base text-gray-700  focus:outline focus:outline-0 sm:text-sm/6"
                          disabled
                        >${item.question}</textarea>
                      </div>

                      <!-- answer -->
                      <details
                        class="${
                          item.answer == null
                            ? "hidden"
                            : "none"
                        } group [&_summary::-webkit-details-marker]:hidden inline-block"
                      >
                        <summary
                          class="flex cursor-pointer items-center rounded-lg text-gray-500 hover:text-gray-700"
                        >
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

                          <span class="text-sm">
                            老師回答
                          </span>
                        </summary>

                        <ul class="px-4">
                          <li>
                            <div
                              class="answer flex items-center text-start rounded-lg px-4 py-2 text-gray-700"
                            >
                              <p>${item.answer}</p>
                            </div>
                          </li>
                        </ul>
                      </details>
                    </div>

                    <!-- open dropdown -->
                    <button
                      class="${
                        item.userInfo.userId != userId
                          ? "hidden"
                          : "none"
                      } question-open-dropdown absolute right-0 top-0 w-8 h-8 group text-gray-700 rounded-full"
                    >
                      <i
                        class="bi bi-three-dots-vertical group-hover:text-gray-500"
                      ></i>
                    </button>

                    <!-- dropdown -->
                    <div
                      class="question-dropdown hidden absolute top-6 end-0 z-10 mt-2 w-28  border border-gray-400 bg-white shadow-lg"
                      role="menu-${index}"
                    >
                      <div class="p-2">
                        <button
                          class="question-edit-btn block rounded-lg px-4 py-2 text-sm text-gray-700 hover:bg-gray-50 hover:text-gray-500"
                          role="menuitem"
                        >
                          <i
                            class="text-xl bi bi-pencil-square pr-2 group-hover:text-gray-500"
                          ></i>
                          編輯
                        </button>

                        <button
                          class="question-delete-btn block rounded-lg px-4 py-2 text-sm text-gray-700 hover:bg-gray-50 hover:text-gray-500"
                          role="menuitem"
                        >
                          <i
                            class="text-xl bi bi-trash pr-2 group-hover:text-gray-500"
                          ></i>
                          刪除
                        </button>
                      </div>
                    </div>
                  </div>`);
      });
    }
    autoResizeTextarea($(".auto-resize"));
  }
  getQuestions();

  // textarea height auto resize
  function autoResizeTextarea(textarea) {
    textarea.each(function () {
      $(this).on("input", function () {
        $(this).height("auto");
        $(this).height(this.scrollHeight + "px");
      });

      // height initial
      $(this).height("auto");
      $(this).height(this.scrollHeight + "px");
    });
  }
  autoResizeTextarea($(".auto-resize"));

  // open dropdown
  $(document).on(
    "click",
    ".question-open-dropdown",
    function () {
      $(this)
        .parent()
        .find("div[role^='menu']")
        .toggleClass("hidden none");
    }
  );

  // edit question
  $(document).on(
    "click",
    ".question-edit-btn",
    function () {
      $(".question-dropdown").addClass("hidden");

      $(this)
        .closest(".question")
        .find("textarea")
        .addClass("border-b border-gray-200")
        .removeAttr("disabled")
        .focus();

      $(`
        <div class="question-edit-save-and-cancel flex justify-end space-x-2 mt-2">
          <button class="question-edit-save w-8 h-8 justify-end rounded-md text-orange-500 hover:text-orange-700 font-semibold">
            <i class="bi bi-check text-[24px] "></i>
          </button>
          <button class="question-edit-cancel w-8 h-8 justify-end rounded-md text-red-500 hover:text-red-700 font-semibold">
            <i class="bi bi-x text-[24px] "></i>
          </button>
        </div>
        `).insertAfter(
        $(this)
          .closest(".question")
          .find("textarea")
          .parent()
      );
    }
  );

  // edit save
  $(document).on(
    "click",
    ".question-edit-save",
    async function () {
      const response = await fetch(
        `http://localhost:8080/questions/update`,
        {
          method: "PUT",
          credentials: "include",
          headers: {
            "Content-Type": "application/json",
          },
          body: JSON.stringify({
            questionId: $(this)
              .closest(".question")
              .attr("id"),
            question: $(this)
              .closest(".question")
              .find("textarea")
              .val(),
          }),
        }
      );

      if (!response.ok) {
        throw new Error("Internal Error");
      } else {
        getQuestions();
      }
    }
  );

  // edit cancel
  $(document).on(
    "click",
    ".question-edit-cancel",
    function () {
      getQuestions();
    }
  );

  // delete question
  $(document).on(
    "click",
    ".question-delete-btn",
    async function () {
      $(this).closest(".question").remove();
      let questionId = $(this)
        .closest(".question")
        .attr("id");
      const response = await fetch(
        `http://localhost:8080/questions/${questionId}/delete`,
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

  $(".tab-link").on("click", function (e) {
    autoResizeTextarea($(".auto-resize"));
  });

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

  // get progress by lessonId
  async function getProgressByLessonId() {
    try {
      const response = await fetch(
        `http://localhost:8080/progress/lesson/${lessonId}`,
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
      $("#courseVideo")[0].currentTime = data.progressTime;
    } catch (error) {
      // data not found
    }
  }

  // get all progress by course
  async function getAllProgressByCourseId() {
    try {
      const response = await fetch(
        `http://localhost:8080/progress/courses/${courseId}`,
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
        $.each(data, function (index, item) {
          if (item.isCompleted) {
            $(`div[data-id="${item.progressId.lessonId}"]`)
              .closest("details")
              .find(".lesson-index")
              .find("i")
              .removeClass("hidden");
          }
        });
      }
    } catch (error) {
      console.log(error);
    }
  }
  getAllProgressByCourseId();

  async function isCreatedProgress() {
    try {
      const response = await fetch(
        `http://localhost:8080/progress/lesson/${lessonId}`,
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
      if (data != null) {
        return true;
      } else {
        return false;
      }
    } catch (error) {
      return false;
    }
  }

  // get last view by courseId
  async function getLastView() {
    try {
      const response = await fetch(
        `http://localhost:8080/progress/courses/${courseId}/last-view`,
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

      lessonId = data.progressId.lessonId;
      getProgressByLessonId();
      const videoSrc = $(`div[data-id='${lessonId}']`).data(
        "src"
      );
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
    } catch (error) {
      // data not found (first time)
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

  // create progress
  async function createProgress() {
    try {
      const response = await fetch(
        `http://localhost:8080/progress/lesson/${lessonId}`,
        {
          method: "POST",
          credentials: "include",
          headers: {
            "Content-Type": "application/json",
          },
          body: JSON.stringify({
            progressTime: Math.floor(
              currenTime[0].currentTime
            ),
            totalDuration: Math.floor(
              currenTime[0].duration
            ),
            isCompleted: isCompleted,
          }),
        }
      );

      if (!response.ok) {
        throw new Error("Internal Error");
      }

      const data = await response.json();

      console.log("progress saved...");
    } catch (error) {
      console.log(error);
    }
  }

  // update progress
  async function updateProgress() {
    try {
      const response = await fetch(
        `http://localhost:8080/progress/lesson/${lessonId}`,
        {
          method: "PUT",
          credentials: "include",
          headers: {
            "Content-Type": "application/json",
          },
          body: JSON.stringify({
            progressTime: Math.floor(
              currenTime[0].currentTime
            ),
            totalDuration: Math.floor(
              currenTime[0].duration
            ),
          }),
        }
      );

      if (!response.ok) {
        throw new Error("Internal Error");
      }

      console.log("progress saved...");
    } catch (error) {
      console.log(error);
    }
  }

  // mark complete
  async function markProgressAsCompleted() {
    try {
      const response = await fetch(
        `http://localhost:8080/progress/lesson/${lessonId}/complete`,
        {
          method: "PUT",
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
  }

  // event listener
  let intervalId;
  const currenTime = $("#courseVideo");

  // save progress every 3s
  $("#courseVideo").on("play", async function () {
    const progressExists = await isCreatedProgress();
    if (progressExists == false) {
      await createProgress();
    }

    if (
      currenTime[0].duration - 1 <
      currenTime[0].currentTime
    ) {
      await markProgressAsCompleted();
      await updateProgress();
      $(`div[data-id="${lessonId}"]`)
        .find("i")
        .removeClass("hidden");
    }
    await updateProgress();
    console.log("playing...");
    intervalId = setInterval(function () {
      updateProgress();
    }, 3000);
  });

  // save progress when pause video
  $("#courseVideo").on("pause", async function () {
    await updateProgress();
    console.log("pause");
    clearInterval(intervalId);
  });

  // save progress when video ended
  $("#courseVideo").on("ended", async function () {
    await markProgressAsCompleted();
    await updateProgress();
    await getAllProgressByCourseId();
    console.log("ended");
    clearInterval(intervalId);
  });
});
