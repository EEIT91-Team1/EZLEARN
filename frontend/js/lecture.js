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

  // get course announcement by course id
  async function getPosts() {
    try {
      const response = await fetch(
        "http://localhost:8080/courses/1/posts",
        {
          method: "GET",
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

        console.log(data);
      } else {
        $("#no-posts").removeClass("hidden");
      }
    } catch (error) {
      console.log(error);
    }
  }
  getPosts();

  // add note
  $("#add-note-btn").on("click", function () {
    $(".note-list").append(`<div
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
              >${$("#add-note-title").val()}</textarea>

              <textarea
                class="note-content disabled:bg-white block w-full resize-none text-base text-gray-700 placeholder:text-gray-400 focus:outline focus:outline-0 sm:text-sm/6"
                disabled
              >${$("#add-note-content").val()}</textarea>
            </div>`);
    $("#add-note-title").val("");
    $("#add-note-content").val("");
  });

  // delete note
  $(document).on("click", ".note-delete-btn", function (e) {
    $(e.currentTarget).closest(".note").remove();
  });

  // edit note
  $(document).on("click", ".note-edit-btn", function (e) {
    $(e.currentTarget)
      .find("i")
      .toggleClass("bi-pencil-square bi-floppy");

    $(e.currentTarget)
      .closest(".note")
      .find(".note-title")
      .toggleClass("text-gray-700 text-gray-400")
      .prop(
        "disabled",
        !$(e.currentTarget)
          .closest(".note")
          .find(".note-title")
          .prop("disabled")
      )
      .focus();

    $(e.currentTarget)
      .closest(".note")
      .find(".note-content")
      .toggleClass("text-gray-700 text-gray-400")
      .prop(
        "disabled",
        !$(e.currentTarget)
          .closest(".note")
          .find(".note-content")
          .prop("disabled")
      );
  });
});
