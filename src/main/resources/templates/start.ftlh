<#import "parts/common.ftlh" as c>

<@c.page>
    <h3 class="my-3">Привет! Добро пожаловать в игру крестики-нолики:)</h3><br>
    <div>
        <form action="/gameplay" method="post">
                <h4>Введите имя первого игрока (ходит крестиками):</h4>
            <div class="col-sm-6 mb-3">
                <div>
                <input type="text" class="form-control ${(nameXNull??)?string('is-invalid', '')}"
                       id="nameX" name="nameX" placeholder="Имя игрока X" required autofocus
                       value="<#if nameX??>${nameX}</#if>"/>
                    <#if nameXNull??>
                        <div class="invalid-feedback">
                            ${nameXNull}
                        </div>
                    </#if>
                </div>
            </div>
                <h4>Введите имя второго игрока (ходит ноликами):</h4>
            <div class="col-sm-6 mb-3">
                <div>
                <input type="text" class="form-control ${(nameONull??)?string('is-invalid', '')}"
                       id="nameO" name="nameO" placeholder="Имя игрока O" required
                       value="<#if nameO??>${nameO}</#if>">
                    <#if nameONull??>
                        <div class="invalid-feedback">
                            ${nameONull}
                        </div>
                    </#if>
                </div>
            </div>
                <div class="col-sm-6 mb-3"><button type="submit" class="btn btn-primary">Начать игру</button></div>
        </form>
    </div>
</@c.page>