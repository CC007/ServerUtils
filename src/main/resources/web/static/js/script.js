function init() {
    var ws = new WebSocket("ws://localhost:4568/chatsocket");

    ws.onmessage = function (evt) {
        $('#messages').append($('<li>').text(evt.data));
    };
    
    $('form').submit(function () {
        ws.send($('#m').val());
        $('#m').val('');
        return false;
    });
}
