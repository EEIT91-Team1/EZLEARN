$(document).ready(function () {
  const video = $("#courseVideo")[0];
  const progressBar = $("#progressBar");

  $(".video-container").on("click", function (e) {
    if ($(e.target).is("button, i, input, li, div")) return;
    $(".play-pause")
      .find("i")
      .toggleClass("bi-play-fill bi-stop-fill");

    if (video.currentTime === video.duration) {
      video.currentTime = 0;
    }

    if (video.paused) {
      video.play();
    } else {
      video.pause();
    }
  });

  $(".play-pause").on("click", function () {
    $(".play-pause")
      .find("i")
      .toggleClass("bi-play-fill bi-stop-fill");

    if (video.currentTime === video.duration) {
      video.currentTime = 0;
    }

    if (video.paused) {
      video.play();
    } else {
      video.pause();
    }
  });

  function formatTime(seconds) {
    const mins = Math.floor(seconds / 60);
    const secs = Math.floor(seconds % 60);
    return `${String(mins).padStart(
      2,
      "0"
    )}:${String(secs).padStart(2, "0")}`;
  }

  $("#courseVideo").on("loadeddata", function () {
    $(".current-time").text(formatTime(video.currentTime));
    $(".video-duration").text(formatTime(video.duration));
  });

  $(".full-screen-btn").on("click", function () {
    $(this)
      .find("i")
      .toggleClass("bi-fullscreen bi-fullscreen-exit");
    if (document.fullscreenElement) {
      $("#courseVideo").addClass("w-[960px] h-[540px]");
      return document.exitFullscreen();
    }
    $(".video-container")[0].requestFullscreen();
    $("#courseVideo").removeClass("w-[960px] h-[540px]");
  });

  $("#courseVideo").on("pause", function () {
    $(".play-pause")
      .find("i")
      .removeClass("bi-stop-fill")
      .addClass("bi-play-fill");
  });

  video.addEventListener("timeupdate", function () {
    $(".current-time").text(formatTime(video.currentTime));
    const progress =
      (video.currentTime / video.duration) * 100;
    progressBar.val(progress);
  });

  progressBar.on("input", function () {
    const value = progressBar.val();
    video.currentTime = (video.duration * value) / 100;
  });

  $(".skip-backward").on("click", function () {
    video.currentTime -= 5;
  });

  $(".skip-forward").on("click", function () {
    video.currentTime += 5;
  });

  $(".playback-speed").on("click", function () {
    $(".speed-options").toggleClass("hidden block");
  });

  $(".speed-option").on("click", function () {
    const speed = $(this).data("speed"); // 取得速度值
    video.playbackRate = speed;
    $(".playback-speed").text(`${speed}x`);
    $(".speed-options").addClass("hidden");
  });

  const sliderThumb = $(".slider-thumb");
  const sliderWrapper = $(".slider-wrapper");

  $(".volume").on("mouseenter", function () {
    $(".volume-slider")
      .css("opacity", "1")
      .addClass("cursor-grab");
  });

  $(".volume-slider").on("mouseleave", function () {
    $(".volume-slider")
      .css("opacity", "0")
      .removeClass("cursor-grab");
  });

  $(".volume").on("click", function () {
    const video = $("#courseVideo")[0];

    video.muted = !video.muted;

    $(this)
      .find("i")
      .toggleClass(
        "bi-volume-down-fill bi-volume-mute-fill"
      );

    if (video.muted) {
      sliderThumb.css("height", "0%");
    } else {
      sliderThumb.css("height", `${video.volume * 100}%`);
    }
  });

  sliderWrapper.on("mousedown", function (e) {
    const video = $("#courseVideo")[0];
    const sliderHeight = sliderWrapper.height();
    const sliderTop = sliderWrapper.offset().top;

    $(".volume-slider").addClass("cursor-grabbing");

    const updateVolume = (event) => {
      const offsetY = Math.min(
        sliderHeight,
        Math.max(
          0,
          sliderHeight - (event.pageY - sliderTop)
        )
      );
      const volume = offsetY / sliderHeight;

      video.volume = volume;

      if (video.muted && volume > 0) {
        video.muted = false;
        $(".volume i")
          .removeClass("bi-volume-mute-fill")
          .addClass("bi-volume-down-fill");
      }

      if (!video.muted && volume === 0) {
        video.muted = true;
        $(".volume i")
          .removeClass("bi-volume-down-fill")
          .addClass("bi-volume-mute-fill");
      }

      sliderThumb.css("height", `${volume * 100}%`);
    };

    updateVolume(e);

    $(document).on("mousemove", updateVolume);

    $(document).on("mouseup", function () {
      $(document).off("mousemove", updateVolume);
      $(".volume-slider")
        .removeClass("cursor-grabbing")
        .addClass("cursor-grab");
    });
  });
});
