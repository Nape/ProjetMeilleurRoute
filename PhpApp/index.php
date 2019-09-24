<!doctype html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport"
              content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
        <meta http-equiv="X-UA-Compatible" content="ie=edge">
<!--        CSS only-->
        <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css"
              integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T" crossorigin="anonymous">
<!--        JS, Popper.js, and jQuery-->
        <script src="https://code.jquery.com/jquery-3.3.1.slim.min.js"
                integrity="sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo"
                crossorigin="anonymous"></script>
        <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js"
                integrity="sha384-UO2eT0CpHqdSJQ6hJty5KVphtPhzWj9WO1clHTMGa3JDZwrnQq4sF86dIHNDz0W1"
                crossorigin="anonymous"></script>
        <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js"
                integrity="sha384-JjSmVgyd0p3pXB1rRibZUAYoIIy6OrQ6VrjIEaFf/nJGzIxFDsf4x0xIM+B07jRM"
                crossorigin="anonymous"></script
    </head>
    <body class="container-fluid" style=" background-image: linear-gradient(to bottom right, gray, ghostwhite); background-repeat: no-repeat; background-attachment: fixed;">
    <div id="bonjour" class="container " style="margin: 0 auto; padding-top: 80px; width: 80%; text-align: center">
    </div>
    <div class="container">
        <div class="row" style="padding-top: 10%">
            <div class="col-2"></div>
            <div class="col-8" style="text-align: center">
                <table class="table table-striped table-dark" style="border-radius: 6px;">
                    <thead class="thead-light" style="border-radius: 6px;">
                    <tr>
                        <form method="post" action="<?=$_SERVER['PHP_SELF'];?>">
                        <th>
                            <select class="form-control" id="select" name="select">
                                <option id="Nadir" value="Nadir">Nadir</option>
                                <option id="Alix" value="Alix" >Alix</option>
                                <option id="Dominique" value="Dominique">Dominique</option>
                            </select>
                        </th>
                        <th>
                            <input type="submit" name="submit" class="btn btn-default btn-dark" ></input>
                        </th>
                        </form>
                    </tr>
                    <tr>
                        <th scope="col"> Numéro d'arrêt</th>
                        <th scope="col"> Adresse</th>
                    </tr>
                    </thead>
                    <tbody>
                    <?php
                    if ( isset( $_POST['submit'] ) )
                    {
                        $employe = $_POST['select'];
                        $url = "http://localhost:4444/Serveur_REST_TP4_war/route/";
                        $url.=$employe;

                            $response = file_get_contents($url);
                            $arrets = explode("\n", $response);
                            foreach ($arrets as $arret)
                            {
                                if (!empty($arret))
                                {
                                    $arretFormat = explode("&",$arret);
                                    echo(
                                        "<tr>
                                            <th scope='row'>" . $arretFormat[0] . "</th>
                                            <th scope='row'>" . $arretFormat[1] . "</th>
                                        </tr>"
                                        );
                                }
                            }
                        ?>
                    </tbody>
                </table>
            </div>
            <?}?>
            <div class="col-2"></div>
        </div>
    </div>
    <script>
        document.getElementById('select').value = "<?php echo $_POST['select'];?>";
        document.getElementById('bonjour').innerHTML = "<?php echo ("<H1>Voici votre route du jour ".$_POST['select']." !</H1>")?>";
    </script>
    </body>
</html>