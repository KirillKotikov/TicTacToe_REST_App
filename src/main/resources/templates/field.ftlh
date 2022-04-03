<#import "parts/common.ftlh" as c>

<@c.page>
    <h4>Игровое поле:</h4><br/>
    <div>
        <#list field as f>
            <h3>${f}</h3>
        </#list>
    </div>
    <br>
    <form action="/gameplay/field" method="post">
        <div>
            <h4 class="form-label" for="coordinate">
                ${name} (${symbol}), твой ход! Введи свободные координаты хода (число от 1 до 9 включительно):
            </h4>
            <div style="width: 30%">
                <input autofocus type="text" name="coordinate" maxlength="1"
                       placeholder="Введи число 1-9" required
                       class="form-control ${(message??)?string('is-invalid', '')}"/>
                <#if message??>
                    <div class="invalid-feedback">
                        ${message}
                    </div>
                </#if>
            </div>
        </div><br/>
        <div>
            <button type="submit" class="btn btn-primary">Отправить координаты</button>
        </div>
    </form>
</@c.page>