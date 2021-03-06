(ns geopressure.core
  (:use [incanter core io charts excel datasets stats])
  (:import [org.jfree.chart ChartPanel])
  (:import [java.awt Component Dimension])
  (:import [javax.swing JFrame JPanel JButton JTextField JInternalFrame])
  (:import [javax.swing JFileChooser JButton JFrame])
  (:import [javax.swing.filechooser FileNameExtensionFilter])
  (:import [java.awt GridLayout]))

;; Scientific analysis functions grouped together in next part

(defn calculate-dxc
  "d = log10(R/60N)/log10(12W/106D) where : R=ROP (ft/hr) N=RPM (rev/min) W=WOB (lbs) D=bit size (inch)"
  [R N D W MW]
  (* 
      (/ (Math/log10 (/ R (* 60 N)))
         (Math/log10 (/ (* 12 W) (* 1000 D))))
   (/ MW 8.4)))


(def data (read-dataset "./data/test-data.csv" :header true))

(def TVD ($ :TVD data))
(def ROP ($ :ROP-mhr data))
(def RPM ($ :RPM data))
(def WOB ($ :WOB-klb data))
(def BIT ($ :BIT-in data))
(def OB ($ :OBG-gcc data))
(def MW ($ :Mud-ppg data))
(def QC-DXC ($ :dxc data))
(def QC-EMW ($ :EMW-ppg data))

(def Dxc (map #(calculate-dxc %1 %2 %3 %4 %5) ROP RPM BIT WOB MW))


(def plot1 (scatter-plot Dxc  TVD))
(def plot2 (scatter-plot QC-DXC TVD))
(def plot3 (scatter-plot QC-EMW TVD))
(def plot4 (scatter-plot OB TVD))

;(def lm1 (linear-model Dxc TVD :intercept))
;(add-lines plot1 Dxc (:fitted lm1))

;(def lm2 (linear-model Dx DEPTH :intercept false))
;(add-lines plot1 DEPTH (:fitted lm2))

(defn show-component
  ([^Component c1 c2 c3 c4]
     "Utility Function to display any Java component in a frame"
     (let [frame (JFrame. "Test Window")]
       (doto frame
         (.setDefaultCloseOperation JFrame/DISPOSE_ON_CLOSE)
         (.setLayout (new GridLayout 1 4 4 4))
         (.add c1)
         (.add c2)
         (.add c3)
         (.add c4)
         (.setSize (Dimension.  640 480))
         (.setExtendedState (. JFrame MAXIMIZED_BOTH))
         (.setVisible true)))))

(defn -main []

  (show-component
   ;(doto (JPanel.) (.add (JTextField.)) (.add (JButton. "test")) )
   (ChartPanel. plot1)
   (ChartPanel. plot2)
   (ChartPanel. plot3)
   (ChartPanel. plot4)
   ))

