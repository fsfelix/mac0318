import java.util.ArrayList;
import java.util.Collections;
import lejos.geom.*;
import lejos.robotics.mapping.LineMap;
public class drawMap {
static Line[] lines = {
new Line((float) 300.0, (float) -85.0, (float) 383.97536527894556, (float) -75.61175851855099),
new Line((float) 383.97536527894556, (float) -75.61175851855099, (float) 414.44326928585525, (float) -24.325667825677844),
new Line((float) 414.44326928585525, (float) -24.325667825677844, (float) 551.1259770181131, (float) -44.28028530506723),
new Line((float) 551.1259770181131, (float) -44.28028530506723, (float) 511.4045810015356, (float) 142.59419038504046),
new Line((float) 511.4045810015356, (float) 142.59419038504046, (float) 355.9487635060773, (float) 43.71196474812174),
new Line((float) 355.9487635060773, (float) 43.71196474812174, (float) 300.0, (float) 50.0),
new Line((float) 300.0, (float) 50.0, (float) 626.9798993405002, (float) -199.87816540381914),
new Line((float) 626.9798993405002, (float) -199.87816540381914, (float) 708.5509716513936, (float) -181.55639735243173),
new Line((float) 708.5509716513936, (float) -181.55639735243173, (float) 762.5941903850404, (float) -211.40458100153563),
new Line((float) 762.5941903850404, (float) -211.40458100153563, (float) 869.4276381871205, (float) -53.01748115852863),
new Line((float) 869.4276381871205, (float) -53.01748115852863, (float) 635.7569240481953, (float) -2.7783708426708853),
new Line((float) 635.7569240481953, (float) -2.7783708426708853, (float) 873.6030833189097, (float) -26.654758133251637),
new Line((float) 873.6030833189097, (float) -26.654758133251637, (float) 874.8446608898694, (float) 8.899371659137747),
new Line((float) 874.8446608898694, (float) 8.899371659137747, (float) 845.1516361790264, (float) 119.71524851040216),
new Line((float) 845.1516361790264, (float) 119.71524851040216, (float) 762.5941903850404, (float) 211.40458100153563),
new Line((float) 762.5941903850404, (float) 211.40458100153563, (float) 652.3250751182255, (float) 51.730933865541985),
new Line((float) 652.3250751182255, (float) 51.730933865541985, (float) 747.5, (float) 220.83647796503183),
new Line((float) 747.5, (float) 220.83647796503183, (float) 644.9871553669774, (float) 51.23126063905252),
new Line((float) 644.9871553669774, (float) 51.23126063905252, (float) 620.0, (float) 53.0),
new Line((float) 620.0, (float) 53.0, (float) 787.717845962876, (float) -26.91532748627293),
new Line((float) 787.717845962876, (float) -26.91532748627293, (float) 806.8844785106205, (float) -82.74191691767835),
new Line((float) 806.8844785106205, (float) -82.74191691767835, (float) 792.736624176141, (float) -31.524251055270774),
new Line((float) 792.736624176141, (float) -31.524251055270774, (float) 827.5082730514789, (float) -52.76328260889499),
new Line((float) 827.5082730514789, (float) -52.76328260889499, (float) 948.1073256510773, (float) -174.08023168195356),
new Line((float) 948.1073256510773, (float) -174.08023168195356, (float) 980.942742169714, (float) -156.99367620804287),
new Line((float) 980.942742169714, (float) -156.99367620804287, (float) 836.6311896062464, (float) -41.14496766047312),
new Line((float) 836.6311896062464, (float) -41.14496766047312, (float) 991.4045810015356, (float) -142.59419038504046),
new Line((float) 991.4045810015356, (float) -142.59419038504046, (float) 803.7453466923799, (float) -14.837739398529738),
new Line((float) 803.7453466923799, (float) -14.837739398529738, (float) 1005.1516361790264, (float) -119.71524851040216),
new Line((float) 1005.1516361790264, (float) -119.71524851040216, (float) 843.948182034982, (float) -28.471565015306016),
new Line((float) 843.948182034982, (float) -28.471565015306016, (float) 841.1179951483434, (float) 29.80923798165726),
new Line((float) 841.1179951483434, (float) 29.80923798165726, (float) 803.8395850071911, (float) 12.675732195219052),
new Line((float) 803.8395850071911, (float) 12.675732195219052, (float) 838.5153186347934, (float) 36.564429232091136),
new Line((float) 838.5153186347934, (float) 36.564429232091136, (float) 803.0188736108369, (float) 22.229067854687912),
new Line((float) 803.0188736108369, (float) 22.229067854687912, (float) 829.2529180260526, (float) 63.04086028853776),
new Line((float) 829.2529180260526, (float) 63.04086028853776, (float) 788.6524758424985, (float) 26.6295824562643),
new Line((float) 788.6524758424985, (float) 26.6295824562643, (float) 780.0, (float) 54.0),
new Line((float) 780.0, (float) 54.0, (float) 330.0, (float) -437.0),
new Line((float) 330.0, (float) -437.0, (float) 173.0063237919571, (float) -249.05725783028592),
new Line((float) 173.0063237919571, (float) -249.05725783028592, (float) 134.6586670046606, (float) -286.0891595299325),
new Line((float) 134.6586670046606, (float) -286.0891595299325, (float) 153.4855911920943, (float) -312.0918295270526),
new Line((float) 153.4855911920943, (float) -312.0918295270526, (float) 100.80751819371244, (float) -338.21535756878524),
new Line((float) 100.80751819371244, (float) -338.21535756878524, (float) 202.97581192435015, (float) -398.67889670202004),
new Line((float) 202.97581192435015, (float) -398.67889670202004, (float) 200.229671048327, (float) -487.2110430352949),
new Line((float) 200.229671048327, (float) -487.2110430352949, (float) 228.23695275641862, (float) -483.06481839811937),
new Line((float) 228.23695275641862, (float) -483.06481839811937, (float) 203.1414961939024, (float) -496.1727193489653),
new Line((float) 203.1414961939024, (float) -496.1727193489653, (float) 305.3342726436498, (float) -460.9818893630466),
new Line((float) 305.3342726436498, (float) -460.9818893630466, (float) 207.7640097033133, (float) -509.61847596331455),
new Line((float) 207.7640097033133, (float) -509.61847596331455, (float) 307.0433625856679, (float) -462.20626063243316),
new Line((float) 307.0433625856679, (float) -462.20626063243316, (float) 198.552545095754, (float) -532.1374859561467),
new Line((float) 198.552545095754, (float) -532.1374859561467, (float) 214.06940722552653, (float) -554.3843745919819),
new Line((float) 214.06940722552653, (float) -554.3843745919819, (float) 146.568350913644, (float) -627.1378844670443),
new Line((float) 146.568350913644, (float) -627.1378844670443, (float) 173.00632379195716, (float) -650.942742169714),
new Line((float) 173.00632379195716, (float) -650.942742169714, (float) 238.3055006423742, (float) -576.2066511224918),
new Line((float) 238.3055006423742, (float) -576.2066511224918, (float) 187.40580961495957, (float) -661.4045810015356),
new Line((float) 187.40580961495957, (float) -661.4045810015356, (float) 290.5, (float) -518.4160068989706),
new Line((float) 290.5, (float) -518.4160068989706, (float) 330.0, (float) -522.0),
new Line((float) 330.0, (float) -522.0, (float) 271.1006283408622, (float) -245.15533911013057),
new Line((float) 271.1006283408622, (float) -245.15533911013057, (float) 268.60639750382575, (float) -391.5971134048582),
new Line((float) 268.60639750382575, (float) -391.5971134048582, (float) 246.6261646075057, (float) -397.2858962401234),
new Line((float) 246.6261646075057, (float) -397.2858962401234, (float) 176.2821560156709, (float) -267.0459083011368),
new Line((float) 176.2821560156709, (float) -267.0459083011368, (float) 250.19076201834278, (float) -438.88200485165663),
new Line((float) 250.19076201834278, (float) -438.88200485165663, (float) 152.5, (float) -279.1635220349682),
new Line((float) 152.5, (float) -279.1635220349682, (float) 59.16352203496814, (float) -372.50000000000006),
new Line((float) 59.16352203496814, (float) -372.50000000000006, (float) 192.58818830696626, (float) -453.5223152841968),
new Line((float) 192.58818830696626, (float) -453.5223152841968, (float) 50.80751819371244, (float) -388.21535756878524),
new Line((float) 50.80751819371244, (float) -388.21535756878524, (float) 199.60799972745116, (float) -464.2071754093296),
new Line((float) 199.60799972745116, (float) -464.2071754093296, (float) 43.568117085469225, (float) -404.4753186789424),
new Line((float) 43.568117085469225, (float) -404.4753186789424, (float) 198.20913959861682, (float) -473.42453848375453),
new Line((float) 198.20913959861682, (float) -473.42453848375453, (float) 207.3650001054211, (float) -586.5630220724445),
new Line((float) 207.3650001054211, (float) -586.5630220724445, (float) 255.3735409869737, (float) -531.5204301442689),
new Line((float) 255.3735409869737, (float) -531.5204301442689, (float) 219.58920387741466, (float) -596.6774829618325),
new Line((float) 219.58920387741466, (float) -596.6774829618325, (float) 280.0, (float) -571.0),
new Line((float) 280.0, (float) -571.0, (float) 8.899371659137728, (float) -254.8446608898694),
new Line((float) 8.899371659137728, (float) -254.8446608898694, (float) 119.7152485104022, (float) -225.15163617902633),
new Line((float) 119.7152485104022, (float) -225.15163617902633, (float) 45.0, (float) -77.94228634059945),
new Line((float) 45.0, (float) -77.94228634059945, (float) 135.12941237946723, (float) -216.25226451988863),
new Line((float) 135.12941237946723, (float) -216.25226451988863, (float) 32.992381304774085, (float) -48.913216780747426),
new Line((float) 32.992381304774085, (float) -48.913216780747426, (float) 156.99367620804287, (float) -200.9427421697141),
new Line((float) 156.99367620804287, (float) -200.9427421697141, (float) 20.141514409482227, (float) -19.450434372851888),
new Line((float) 20.141514409482227, (float) -19.450434372851888, (float) 225.15163617902638, (float) -119.71524851040212),
new Line((float) 225.15163617902638, (float) -119.71524851040212, (float) 31.00985648593496, (float) -11.286664729747045),
new Line((float) 31.00985648593496, (float) -11.286664729747045, (float) 201.86495614704697, (float) -57.88384472156982),
new Line((float) 201.86495614704697, (float) -57.88384472156982, (float) 90.60231327712313, (float) -15.975632345357553),
new Line((float) 90.60231327712313, (float) -15.975632345357553, (float) 254.37883281625517, (float) -17.787900804752013),
new Line((float) 254.37883281625517, (float) -17.787900804752013, (float) 60.962840448164854, (float) 2.128869298852578),
new Line((float) 60.962840448164854, (float) 2.128869298852578, (float) 253.60308331890968, (float) 26.654758133251647),
new Line((float) 253.60308331890968, (float) 26.654758133251647, (float) 249.42763818712046, (float) 53.01748115852868),
new Line((float) 249.42763818712046, (float) 53.01748115852868, (float) 89.26720681739172, (float) 22.256814395169442),
new Line((float) 89.26720681739172, (float) 22.256814395169442, (float) 242.5194116552642, (float) 78.79933356561156),
new Line((float) 242.5194116552642, (float) 78.79933356561156, (float) 232.95409169886324, (float) 103.71784398432905),
new Line((float) 232.95409169886324, (float) 103.71784398432905, (float) 83.58784630582255, (float) 40.76851665138423),
new Line((float) 83.58784630582255, (float) 40.76851665138423, (float) 220.83647796503186, (float) 127.50000000000003),
new Line((float) 220.83647796503186, (float) 127.50000000000003, (float) 135.1294123794673, (float) 216.25226451988863),
new Line((float) 135.1294123794673, (float) 216.25226451988863, (float) 76.00000000000009, (float) 131.63586137523467),
new Line((float) 76.00000000000009, (float) 131.63586137523467, (float) 119.71524851040218, (float) 225.15163617902635),
new Line((float) 119.71524851040218, (float) 225.15163617902635, (float) 12.202099292274, (float) 27.40636372927807),
new Line((float) 12.202099292274, (float) 27.40636372927807, (float) 30.781812899310182, (float) 84.57233587073179),
new Line((float) 30.781812899310182, (float) 84.57233587073179, (float) 0.0, (float) 87.00000000000003),
new Line((float) 0.0, (float) 87.00000000000003, (float) 64.27347824077077, (float) -204.181752677772),
new Line((float) 64.27347824077077, (float) -204.181752677772, (float) 106.35158247189878, (float) -193.40756205858935),
new Line((float) 106.35158247189878, (float) -193.40756205858935, (float) 24.1842582644133, (float) -197.38214497022335),
new Line((float) 24.1842582644133, (float) -197.38214497022335, (float) 125.76669548622209, (float) -145.43113308170047),
new Line((float) 125.76669548622209, (float) -145.43113308170047, (float) 21.33010259284123, (float) -190.1858212927575),
new Line((float) 21.33010259284123, (float) -190.1858212927575, (float) 152.4352349808857, (float) -8.052207196402577),
new Line((float) 152.4352349808857, (float) -8.052207196402577, (float) 53.20275857426219, (float) -116.55770688506622),
new Line((float) 53.20275857426219, (float) -116.55770688506622, (float) 122.94155877284282, (float) 11.841528598915218),
new Line((float) 122.94155877284282, (float) 11.841528598915218, (float) 12.524497782026515, (float) -170.62604014512743),
new Line((float) 12.524497782026515, (float) -170.62604014512743, (float) 82.65669532088839, (float) 30.626668934289995),
new Line((float) 82.65669532088839, (float) 30.626668934289995, (float) 5.364503582254855, (float) -174.38952831233718),
new Line((float) 5.364503582254855, (float) -174.38952831233718, (float) 48.45903993137145, (float) 40.43268882100381),
new Line((float) 48.45903993137145, (float) 40.43268882100381, (float) 0.882563724884676, (float) -131.1849534458504),
new Line((float) 0.882563724884676, (float) -131.1849534458504, (float) -22.346342031909103, (float) 45.38388345013851),
new Line((float) -22.346342031909103, (float) 45.38388345013851, (float) -13.751473259944476, (float) -172.12371176762883),
new Line((float) -13.751473259944476, (float) -172.12371176762883, (float) -30.51586875465273, (float) -129.10620199732378),
new Line((float) -30.51586875465273, (float) -129.10620199732378, (float) -91.55844122715712, (float) -208.99494936611669),
new Line((float) -91.55844122715712, (float) -208.99494936611669, (float) 19.917155521093434, (float) -16.71247785185001),
new Line((float) 19.917155521093434, (float) -16.71247785185001, (float) 211.4045810015356, (float) -142.59419038504046),
new Line((float) 211.4045810015356, (float) -142.59419038504046, (float) 23.74534669237994, (float) -14.83773939852972),
new Line((float) 23.74534669237994, (float) -14.83773939852972, (float) 220.83647796503186, (float) -127.49999999999999),
new Line((float) 220.83647796503186, (float) -127.49999999999999, (float) 251.12597701811308, (float) 44.28028530506725),
new Line((float) 251.12597701811308, (float) 44.28028530506725, (float) 156.99367620804284, (float) 200.94274216971417),
new Line((float) 156.99367620804284, (float) 200.94274216971417, (float) 2.842170943040401e-14, (float) 17.00000000000003),
new Line((float) 2.842170943040401e-14, (float) 17.00000000000003, (float) 79.02675433914848, (float) -304.7760126118775),
new Line((float) 79.02675433914848, (float) -304.7760126118775, (float) 116.91038968407264, (float) -335.17352194298155),
new Line((float) 116.91038968407264, (float) -335.17352194298155, (float) 145.14089971617324, (float) -308.99436728933364),
new Line((float) 145.14089971617324, (float) -308.99436728933364, (float) 203.28027557481693, (float) -509.6904326034087),
new Line((float) 203.28027557481693, (float) -509.6904326034087, (float) 339.7647249130239, (float) -430.89109903779706),
new Line((float) 339.7647249130239, (float) -430.89109903779706, (float) 403.86587773519796, (float) -286.91755254953983),
new Line((float) 403.86587773519796, (float) -286.91755254953983, (float) 189.239036670033, (float) -261.6238747876858),
new Line((float) 189.239036670033, (float) -261.6238747876858, (float) 401.3887714344013, (float) -215.98250911122096),
new Line((float) 401.3887714344013, (float) -215.98250911122096, (float) 379.45527622257583, (float) -148.47815198507348),
new Line((float) 379.45527622257583, (float) -148.47815198507348, (float) 232.5352077758099, (float) -212.76279441628824)
}; 
    public static void main(String[] args) {
        Rectangle bounds = new Rectangle(-800, -800, 2000, 2000);
        //Line[] mylines = new Line[N/2];
        LineMap finalMap = new LineMap(lines, bounds);

        try
            {
                finalMap.createSVGFile("mapaSemFlipY.svg");
                finalMap.flip().createSVGFile("mapaFlipY.svg");
            }

        catch (Exception e)
            {
                System.out.print("Exception caught: ");
                System.out.println(e.getMessage());
                System.exit(1);
            }
    }
} 
