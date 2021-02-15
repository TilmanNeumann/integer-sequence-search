/*
 * integer-sequence-search (ISS) is an offline OEIS sequence search engine.
 * Copyright (C) 2018 Tilman Neumann (www.tilman-neumann.de)
 *
 * This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program;
 * if not, see <http://www.gnu.org/licenses/>.
 */
package de.tilman_neumann.iss;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.sql.SQLException;
import java.util.List;

import org.apache.log4j.Logger;

import de.tilman_neumann.iss.main.OeisLookupEngine;
import de.tilman_neumann.iss.main.OeisLookupEngineInMemory;
import de.tilman_neumann.iss.main.OeisLookupMode;
import de.tilman_neumann.iss.sequence.OEISSequence;
import de.tilman_neumann.iss.sequence.SequenceValues_BigIntListImpl;
import de.tilman_neumann.jml.base.BigIntList;
import de.tilman_neumann.util.ConfigUtil;

/**
 * Machine for complex searches in the OEIS sequences.
 * @author Tilman Neumann
 */
//A000009 (partitions of n into distinct parts): 1 1 1 2 2 3 4 5 6 8 10 12 15 18 22 27 32 38 46 54 64 76 89 104 122 142 165 192 222 256 296 340 390 448 512 585 668 760 864 982 1113 1260 1426 1610 1816 2048 2304 2590 2910 3264 3658 4097 4582 5120 5718 
//A000010 (Euler totient function): 1 1 2 2 4 2 6 4 6 4 10 4 12 6 8 8 16 6 18 8 12 10 22 8 20 12 18 12 28 8 30 16 20 16 24 12 36 18 24 16 40 12 42 20 24 22 46 16 42 20 32 24 52 18 40 24 36 28 58 16 60 30 36 32 48 20 66 32 44
//A000032 (Lucas numbers): 2 1 3 4 7 11 18 29 47 76 123 199 322 521 843 1364 2207 3571 5778 9349 15127 24476 39603 64079 103682 167761 271443 439204 710647 1149851 1860498 3010349 4870847 7881196 12752043 20633239 33385282
//A000040 (primes): 2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41, 43, 47, 53, 59, 61, 67, 71, 73, 79, 83, 89, 97, 101, 103, 107, 109, 113, 127, 131, 137, 139, 149, 151, 157, 163, 167, 173, 179, 181, 191, 193, 197, 199, 211, 223, 227, 229, 233, 239, 241, 251, 257, 263, 269, 271 
//A000041 (partition numbers): 1 1 2 3 5 7 11 15 22 30 42 56 77 101 135 176 231 297 385 490 627 792 1002 1255 1575 1958 2436 3010 3718 4565 5604 6842 8349 10143 12310 14883 17977 21637 26015 31185 37338 44583 53174 63261 75175 89134
//A000043 (Primes p such that 2^p - 1 is (a Mersenne) prime): 2 3 5 7 13 17 19 31 61 89 107 127 521 607 1279 2203 2281 3217 4253 4423 9689 9941 11213 19937 21701 23209 44497 86243 110503 132049 216091 756839 859433 1257787 1398269 2976221 3021377 6972593 13466917
//A000045 (Fibonacci numbers): 0 1 1 2 3 5 8 13 21 34 55 89 144 233 377 610 987 1597 2584 4181 6765 10946 17711 28657 46368 75025 121393 196418 317811 514229 832040 1346269 2178309 3524578 5702887 9227465 14930352 24157817 39088169
//A000055 (Number of trees with n unlabeled nodes): 1 1 1 1 2 3 6 11 23 47 106 235 551 1301 3159 7741 19320 48629 123867 317955 823065 2144505 5623756 14828074 39299897 104636890 279793450 751065460 2023443032 5469566585 14830871802 40330829030 109972410221
//A000056 (Order of the group SL(2,Z_n): 1 6 24 48 120 144 336 384 648 720 1320 1152 2184 2016 2880 3072 4896 3888 6840 5760 8064 7920 12144 9216 15000 13104 17496 16128 24360 17280 29760 24576 31680 29376 40320 31104
//A000058 (Sylvester's sequence): 2 3 7 43 1807 3263443 10650056950807 113423713055421844361000443 12864938683278671740537145998360961546653259485195807 165506647324519964198468195444439180017513152706377497841851388766535868639572406808911988131737645185443
//A000070 (Sum_{k=0..n} #(partitions of k)): 1 2 4 7 12 19 30 45 67 97 139 195 272 373 508 684 915 1212 1597 2087 2714 3506 4508 5763 7338 9296 11732 14742 18460 23025 28629 35471 43820 53963 66273 81156 99133 120770 146785 177970 215308 259891 313065 376326 451501
//A000079 (powers of 2): 1 2 4 8 16 32 64 128 256 512 1024 2048 4096 8192 16384 32768 65536 131072 262144 524288 1048576 2097152 4194304 8388608 16777216 33554432 67108864 134217728 268435456 536870912 1073741824 2147483648 4294967296 8589934592
//A000081 (Number of rooted trees with n nodes): (0,) 1, 1, 2, 4, 9, 20, 48, 115, 286, 719, 1842, 4766, 12486, 32973, 87811, 235381, 634847, 1721159, 4688676, 12826228, 35221832, 97055181, 268282855, 743724984, 2067174645, 5759636510, 16083734329, 45007066269, 126186554308, 354426847597
//A000104 (Number of n-celled polyominoes without holes): 1, 1, 1, 2, 5, 12, 35, 107, 363, 1248, 4460, 16094, 58937, 217117, 805475, 3001127, 11230003, 42161529, 158781106, 599563893, 2269506062, 8609442688, 32725637373, 124621833354, 475368834568, 1816103345752, 6948228104703
//A000105 (Number of polyominoes (or square animals) with n cells): 1, 1, 1, 2, 5, 12, 35, 108, 369, 1285, 4655, 17073, 63600, 238591, 901971, 3426576, 13079255, 50107909, 192622052, 742624232, 2870671950, 11123060678, 43191857688, 168047007728, 654999700403, 2557227044764, 9999088822075, 39153010938487, 153511100594603
//A000107 (Number of rooted trees with n nodes and a single labeled node; pointed rooted trees; vertebrates): 0, 1, 2, 5, 13, 35, 95, 262, 727, 2033, 5714, 16136, 45733, 130046, 370803, 1059838, 3035591, 8710736, 25036934, 72069134, 207727501, 599461094, 1731818878, 5008149658, 14496034714, 41993925955, 121747732406
//A000108 (Catalan numbers): 1 1 2 5 14 42 132 429 1430 4862 16796 58786 208012 742900 2674440 9694845 35357670 129644790 477638700 1767263190 6564120420 24466267020 91482563640 343059613650 1289904147324
//A000110 (Bell numbers): 1, 1, 2, 5, 15, 52, 203, 877, 4140, 21147, 115975, 678570, 4213597, 27644437, 190899322, 1382958545, 10480142147, 82864869804, 682076806159, 5832742205057, 51724158235372, 474869816156751, 4506715738447323
//A000111 (Euler or up/down numbers: expansion of sec x + tan x . Also number of alternating permutations on n letters): 1, 1, 1, 2, 5, 16, 61, 272, 1385, 7936, 50521, 353792, 2702765, 22368256, 199360981, 1903757312, 19391512145, 209865342976, 2404879675441, 29088885112832, 370371188237525, 4951498053124096, 69348874393137901
//A000112 (Number of partially ordered sets ("posets") with n unlabeled elements): 1, 1, 2, 5, 16, 63, 318, 2045, 16999, 183231, 2567284, 46749427, 1104891746, 33823827452, 1338193159771, 68275077901156, 4483130665195087
//A000113 (Number of transformation groups of order n): 1, 3, 4, 3, 6, 12, 8, 6, 4, 18, 12, 12, 14, 24, 24, 6, 18, 12, 20, 18, 32, 36, 24, 24, 30, 42, 12, 24, 30, 72, 32, 12, 48, 54, 48, 12, 38, 60, 56, 36, 42, 96, 44, 36, 24, 72, 48, 24, 56, 90, 72, 42, 54, 36, 72, 48, 80, 90, 60, 72
//A000123 (Number of binary partitions: number of partitions of 2n into powers of 2): 1, 2, 4, 6, 10, 14, 20, 26, 36, 46, 60, 74, 94, 114, 140, 166, 202, 238, 284, 330, 390, 450, 524, 598, 692, 786, 900, 1014, 1154, 1294, 1460, 1626, 1828, 2030, 2268, 2506, 2790, 3074, 3404, 3734, 4124, 4514, 4964, 5414, 5938, 6462, 7060, 7658, 8350, 9042
//A000129 (Pell numbers): 0, 1, 2, 5, 12, 29, 70, 169, 408, 985, 2378, 5741, 13860, 33461, 80782, 195025, 470832, 1136689, 2744210, 6625109, 15994428, 38613965, 93222358, 225058681, 543339720, 1311738121, 3166815962, 7645370045, 18457556052, 44560482149
//A000133 (Number of Boolean functions of n variables): 2, 5, 30, 2288, 67172352, 144115192303714304, 1329227995784915891206435945914040320, 226156424291633194186662080095093570364871077725232774230036394136943198208
//A000135 (Number of partitions into non-integral powers): 1, 2, 6, 13, 24, 42, 73, 125, 204, 324
//A000136 (Number of ways of folding a strip of n labeled stamps): 1, 2, 6, 16, 50, 144, 462, 1392, 4536, 14060, 46310, 146376, 485914, 1557892, 5202690, 16861984, 56579196, 184940388, 622945970, 2050228360, 6927964218, 22930109884, 77692142980, 258360586368, 877395996200, 2929432171328, 9968202968958, 33396290888520, 113837957337750
//A000137 (Series-parallel numbers): 1, 2, 6, 18, 58, 186, 614, 2034, 6818, 22970, 77858, 264970, 905294, 3102434, 10661370, 36722642, 126752218, 438294018, 1518032598, 5265341314, 18286911130, 63586988434, 221342104842, 771235606050, 2689688538646, 9388096331642
//A000142 (factorial): 1 1 2 6 24 120 720 5040 40320 362880 3628800 39916800 479001600 6227020800 87178291200 1307674368000 20922789888000 355687428096000 6402373705728000 121645100408832000 2432902008176640000 
//A000146 (von Staudt-Clausen representation of Bernoulli numbers: a(n) = Bernoulli(2n) + Sum_{(p-1)|2n} 1/p): 1, 1, 1, 1, 1, 1, 2, -6, 56, -528, 6193, -86579, 1425518, -27298230, 601580875, -15116315766, 429614643062, -13711655205087, 488332318973594, -19296579341940067, 841693047573682616, -40338071854059455412
//A000148 (Number of partitions into non-integral powers): 1, 2, 7, 15, 28, 45, 70, 100, 138
//A000157 (Number of Boolean functions of n variables): 1, 2, 7, 111, 308063, 100126976263592
//A000161 (Number of partitions of n into 2 squares): 1, 1, 1, 0, 1, 1, 0, 0, 1, 1, 1, 0, 0, 1, 0, 0, 1, 1, 1, 0, 1, 0, 0, 0, 0, 2, 1, 0, 0, 1, 0, 0, 1, 0, 1, 0, 1, 1, 0, 0, 1, 1, 0, 0, 0, 1, 0, 0, 0, 1, 2, 0, 1, 1, 0, 0, 0, 0, 1, 0, 0, 1, 0, 0, 1, 2, 0, 0, 1, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0, 0, 1, 1, 1, 0, 0, 2, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 1, 1, 0, 2, 1, 0, 0, 1, 0, 1, 0
//A000162 (Number of 3-dimensional polyominoes (or polycubes) with n cells): 1, 1, 2, 8, 29, 166, 1023, 6922, 48311, 346543, 2522522, 18598427, 138462649, 1039496297, 7859514470, 59795121480
//A000163 (Series-parallel numbers): 2, 8, 34, 136, 538, 2080, 7970, 30224, 113874
//A000165 (Double factorial numbers: (2n)!! = 2^n*n!): 1, 2, 8, 48, 384, 3840, 46080, 645120, 10321920, 185794560, 3715891200, 81749606400, 1961990553600, 51011754393600, 1428329123020800, 42849873690624000, 1371195958099968000, 46620662575398912000
//A000166 (Subfactorial or rencontres numbers): 1, 0, 1, 2, 9, 44, 265, 1854, 14833, 133496, 1334961, 14684570, 176214841, 2290792932, 32071101049, 481066515734, 7697064251745, 130850092279664, 2355301661033953, 44750731559645106, 895014631192902121, 18795307255050944540
//A000169 (Number of labeled rooted trees with n nodes: n^(n-1) ): 1, 2, 9, 64, 625, 7776, 117649, 2097152, 43046721, 1000000000, 25937424601, 743008370688, 23298085122481, 793714773254144, 29192926025390625, 1152921504606846976, 48661191875666868481, 2185911559738696531968
//A000176 (Generalized tangent numbers): 2, 11, 46, 128, 272, 522, 904, 1408, 2160, 3154, 4306, 5888, 7888, 10012, 12888, 16384, 19680, 24354, 29866, 34816, 41888, 49778, 56744, 66816, 78000, 87358, 100602, 115712, 128112, 145804, 165712, 180224, 203040, 228964, 246932, 276480
//A000178 (Superfactorials: product of first n factorials): 1, 1, 2, 12, 288, 34560, 24883200, 125411328000, 5056584744960000, 1834933472251084800000, 6658606584104736522240000000, 265790267296391946810949632000000000, 127313963299399416749559771247411200000000000
//A000182 (Tangent (or "Zag") numbers: expansion of tan x. Also expansion of tanh(x)): 1, 2, 16, 272, 7936, 353792, 22368256, 1903757312, 209865342976, 29088885112832, 4951498053124096, 1015423886506852352, 246921480190207983616, 70251601603943959887872, 23119184187809597841473536
//A000183 (Number of discordant permutations of length n): 0, 0, 0, 1, 2, 20, 144, 1265, 12072, 126565, 1445100, 17875140, 238282730, 3407118041, 52034548064, 845569542593, 14570246018686, 265397214435860, 5095853023109484, 102877234050493609, 2178674876680100744
//A000184 (Number of rooted planar maps with n edges): 2, 22, 164, 1030, 5868, 31388, 160648, 795846, 3845020, 18211380, 84876152, 390331292, 1775032504
//A000187 (Generalized Euler numbers): 2, 30, 3522, 1066590, 604935042, 551609685150, 737740947722562, 1360427147514751710, 3308161927353377294082, 10256718523496425979562270, 39490468691102039103925777602, 184856411587530526077816051412830
//A000197 ((n!)!): 1, 1, 2, 720, 620448401733239439360000
//A000200 (Number of bicentered hydrocarbons with n atoms): 0, 0, 1, 0, 1, 1, 3, 3, 9, 15, 38, 73, 174, 380, 915, 2124, 5134, 12281, 30010, 73401, 181835, 452165, 1133252, 2851710, 7215262, 18326528, 46750268, 119687146, 307528889, 792716193, 2049703887, 5314775856, 13817638615, 36012395538
//A000203 (sigma function = sum of divisors): 1, 3, 4, 7, 6, 12, 8, 15, 13, 18, 12, 28, 14, 24, 24, 31, 18, 39, 20, 42, 32, 36, 24, 60, 31, 42, 40, 56, 30, 72, 32, 63, 48, 54, 48, 91, 38, 60, 56, 90, 42, 96, 44, 84, 78, 72, 48, 124, 57, 93, 72, 98, 54, 120, 72, 120, 80, 90, 60, 168, 62, 96, 104, 127, 84, 144, 68, 126, 96, 144 
//A000204 (Lucas numbers (beginning with 1)): 1, 3, 4, 7, 11, 18, 29, 47, 76, 123, 199, 322, 521, 843, 1364, 2207, 3571, 5778, 9349, 15127, 24476, 39603, 64079, 103682, 167761, 271443, 439204, 710647, 1149851, 1860498, 3010349, 4870847, 7881196, 12752043
//XXX: Check for interesting entries above A000204. ----------------------------------------------------

//A000252 (Number of invertible 2 X 2 matrices mod n): 1, 6, 48, 96, 480, 288, 2016, 1536, 3888, 2880, 13200, 4608, 26208, 12096, 23040, 24576, 78336, 23328, 123120, 46080, 96768, 79200, 267168, 73728, 300000, 157248, 314928, 193536, 682080, 138240, 892800, 393216, 633600, 470016, 967680, 373248
//A000292 (Tetrahedral (or pyramidal) numbers): 0, 1, 4, 10, 20, 35, 56, 84, 120, 165, 220, 286, 364, 455, 560, 680, 816, 969, 1140, 1330, 1540, 1771, 2024, 2300, 2600, 2925, 3276, 3654, 4060, 4495, 4960, 5456, 5984, 6545, 7140, 7770, 8436, 9139, 9880, 10660, 11480, 12341, 13244, 14190, 15180
//A000312 (n^n): 1, 1, 4, 27, 256, 3125, 46656, 823543, 16777216, 387420489, 10000000000, 285311670611, 8916100448256, 302875106592253, 11112006825558016, 437893890380859375, 18446744073709551616, 827240261886336764177
//A000396 (perfect numbers): 6, 28, 496, 8128, 33550336, 8589869056, 137438691328, 2305843008139952128, 2658455991569831744654692615953842176, 191561942608236107294793378084303638130997321548169216
//A000432 (Series-parallel numbers): 8, 52, 288, 1424, 6648, 29700, 128800, 545600
//A000608 (Number of connected partially ordered sets with n unlabeled elements): 1, 1, 1, 3, 10, 44, 238, 1650, 14512, 163341, 2360719, 43944974, 1055019099, 32664984238, 1303143553205, 66900392672168, 4413439778321689
//A000954 (Conjecturally largest even integer which is an unordered sum of two primes in exactly n ways) 2, 12, 68, 128, 152, 188, 332, 398, 368, 488, 632, 692, 626, 992, 878, 908, 1112, 998, 1412, 1202, 1448, 1718, 1532, 1604, 1682, 2048, 2252, 2078, 2672, 2642, 2456, 2936, 2504, 2588, 2978, 3092, 3032, 3218, 3272, 3296, 3632, 3548, 3754, 4022, 4058, 4412
//A001348 (Mersenne numbers: 2^p - 1, where p is prime): 3, 7, 31, 127, 2047, 8191, 131071, 524287, 8388607, 536870911, 2147483647, 137438953471, 2199023255551, 8796093022207, 140737488355327, 9007199254740991, 576460752303423487, 2305843009213693951
//A001396 (2n-step walks on diamond lattice): 4, 28, 188, 1428, 10708
//A001597 (perfect powers): 1, 4, 8, 9, 16, 25, 27, 32, 36, 49, 64, 81, 100, 121, 125, 128, 144, 169, 196, 216, 225, 243, 256, 289, 324, 343, 361, 400, 441, 484, 512, 529, 576, 625, 676, 729, 784, 841, 900, 961, 1000, 1024, 1089, 1156, 1225, 1296, 1331, 1369, 1444, 1521, 1600, 1681, 1728, 1764
//A001923 (Sum k^k, k=1..n): 0, 1, 5, 32, 288, 3413, 50069, 873612, 17650828, 405071317, 10405071317, 295716741928, 9211817190184, 312086923782437, 11424093749340453, 449317984130199828, 18896062057839751444, 846136323944176515621 
//A001970 (Functional determinants; partitions of partitions; Euler transform applied twice to all 1's sequence): 1, 1, 3, 6, 14, 27, 58, 111, 223, 424, 817, 1527, 2870, 5279, 9710, 17622, 31877, 57100, 101887, 180406, 318106, 557453, 972796, 1688797, 2920123, 5026410, 8619551, 14722230, 25057499, 42494975, 71832114, 121024876
//A002321 (Mertens's function): 1 0 -1 -1 -2 -1 -2 -2 -2 -1 -2 -2 -3 -2 -1 -1 -2 -2 -3 -3 -2 -1 -2 -2 -2 -1 -1 -1 -2 -3 -4 -4 -3 -2 -1 -1 -2 -1 0 0 -1 -2 -3 -3 -3 -2 -3 -3 -3 -3 -2 -2 -3 -3 -2 -2 -1 0 -1 -1 -2 -1 -1 -1 0 -1 -2 -2 -1 -2 -3 -3 -4 -3 -3 -3 -2 -3 -4 -4 -4
//A003040 (Highest degree of an irreducible representation of symmetric group S_n of degree n): 1, 1, 2, 3, 6, 16, 35, 90, 216, 768, 2310, 7700, 21450, 69498, 292864, 1153152, 4873050, 16336320, 64664600, 249420600, 1118939184, 5462865408, 28542158568, 117487079424, 547591590000, 2474843571200, 12760912164000, 57424104738000, 295284192942320
//A003577 (Dowling numbers): 1, 2, 9, 63, 536, 5307, 60389, 775988, 11062391, 172638727, 2921519374, 53221709973, 1037320865141, 21517178350762, 472862758184789, 10966587174511443, 267502464814857936, 6842498829509972687
//A005011 (Shifts one place left under 5th order binomial transform; invBinomial(A003577)):
//A007013 (a(0) = 2; for n >= 0, a(n+1) = 2^a(n) - 1): 2, 3, 7, 127, 170141183460469231731687303715884105727
//A008683 (Moebius function): 1 -1 -1 0 -1 1 -1 0 0 1 -1 0 -1 1 1 0 -1 0 -1 0 1 1 -1 0 0 1 0 0 -1 -1 -1 0 1 1 1 0 -1 1 1 0 -1 -1 -1 0 0 1 -1 0 0 0 1 0 -1 0 1 0 1 1 -1 0 -1 1 0 0 1 -1 -1 0 1 -1 -1 0 -1 1 0 0 1
//A069856 (inverse binomial transform of n^n; E.g.f.: exp(x)/(1+LambertW(x))): 1, 0, 3, -17, 169, -2079, 31261, -554483, 11336753, -262517615, 6791005621, -194103134499, 6074821125385, -206616861429575, 7588549099814957, -299320105069298459, 12619329503201165281, -566312032570838608863, 26952678355224681891685
//A100262 (sum_{k=0..n} k^k (n-k)^(n-k)): 1, 2, 9, 62, 582, 6978, 102339, 1779222, 35809052, 819103178, 20987183525, 595341928814, 18519658804818, 626784970780690, 22926284614808071, 901188628763393606, 37882728189752349304, 1695744102631158083866
//A110132 (Floor(n/2)^ceiling(n/2): 1, 0, 1, 1, 4, 8, 27, 81, 256, 1024, 3125, 15625, 46656, 279936, 823543, 5764801, 16777216, 134217728, 387420489, 3486784401, 10000000000, 100000000000, 285311670611, 3138428376721, 8916100448256, 106993205379072
//A110138 (Ceiling(n/2)^floor(n/2); invBinomial of A069856): 1,1,1,2,4,9,27,64,256,625,3125,7776,46656,117649,823543,2097152,16777216,43046721,387420489,1000000000,10000000000,25937424601,285311670611,743008370688,8916100448256,23298085122481,302875106592253
//...
//
// TODO: read configuration parameters from file
public class IntegerSequenceSearch {

	private static final Logger LOG = Logger.getLogger(IntegerSequenceSearch.class);
	
	/**
	 * OEIS search application.
	 * @param args ignored
	 * @throws ClassNotFoundException 
	 * @throws SQLException 
	 */
	public static void main(String[] args) throws SQLException, ClassNotFoundException {
    	ConfigUtil.initProject();

    	// initialize search machine
		OeisLookupEngine searchEngine = new OeisLookupEngineInMemory();
//		OeisSearchEngine searchEngine = new OeisSearchEngineWithDB();
		searchEngine.init();

		while(true) {
			String input;
			try {
				LOG.info("Insert A-number or comma-separated sequence:");
				BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
				String line = in.readLine();
				input = line.trim();
				//LOG.debug("input = >" + input + "<");
			} catch (IOException ioe) {
				LOG.error("io-error occuring on input: " + ioe.getMessage());
				continue;
			}
			
			// try to remove sequence from database for the lookup process to avoid matches with itself
			OEISSequence lookupSeq = searchEngine.removeSequence(input);
			boolean isInDatabase = (lookupSeq != null);
			if (!isInDatabase) {
				try {
					List<BigInteger> values = BigIntList.valueOf(input);
					// bigint-impl. should be the fastest Sequence implementation
					// because we will need the values to compute transforms
					lookupSeq = new OEISSequence("input sequence", new SequenceValues_BigIntListImpl(values));
				} catch (IllegalArgumentException iae) {
					LOG.error("Sequence " + input + " is not in database.");
					continue;
				}
			}
			if (lookupSeq.size()<3) {
				LOG.error("Please enter at least 3 terms of your sequence.");
				continue;
			}
			// do the search...
			searchEngine.lookup(lookupSeq, OeisLookupMode.COMPLEX_RELATIONS);
			
			// if the lookup sequence was removed from the database,
			// add it again for further lookups:
			if (isInDatabase) {
				searchEngine.addSequence(lookupSeq);
			}
		}
	}
}
