window.onload = () => {
  liff.init(data => {
      language.innerText = data.language;
      context_type.innerText = data.context.type;
      context_viewType.innerText = data.context.viewType;
      context_userId.innerText = data.context.userId;
      if (data.context.type === 'utou') {
        context_utouId.innerText = data.context.utouId;
      } else if (data.context.type === 'room') {
        context_roomId.innerText = data.context.roomId;
      } else if (data.context.type === 'group') {
        context_groupId.innerText = data.context.groupId;
      }
      location_hash.innerText = window.location.hash;

      closeBtn.onclick = () => {
        liff.closeWindow();
      };

      profileBtn.onclick = () => {
        liff.getProfile()
            .then(data => {
              userId.innerText = data.userId;
              displayName.innerText = data.displayName;
              pictureUrl.src = data.pictureUrl;
              statusMessage.innerText = data.statusMessage;
            })
            .catch(err => {
              console.error(err);
            });
      };

      sendTextBtn.onclick = () => {
        liff.sendMessages([{
          type: 'text',
          text: textMessage.value
        }]).then(() => {
          console.log('sent');
        }).catch((err) => {
          console.log(err);
        });
      };
    },
    err => {
      console.error(err);
    });
};
