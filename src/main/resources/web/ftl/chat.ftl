<#ftl strip_whitespace = true>

<#assign charset="UTF-8">
<#assign title="Chat client">
<!DOCTYPE html>
<html>
    <head>
        <meta charset="${charset}">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
                
        <link rel="stylesheet" href="/css/style.css">
        
        <script src="https://code.jquery.com/jquery-1.11.1.js" type="application/javascript"></script>
        <script src="js/script.js" type="application/javascript"></script>
        
        <title>${title}</title>
    </head>
    <body onload="init();">
        <ul id="messages">
        </ul>
        <form action="">
            <input id="m" autocomplete="off" /><button >Send</button>
        </form>
    </body>
</html>
